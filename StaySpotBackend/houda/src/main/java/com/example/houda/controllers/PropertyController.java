package com.example.houda.controllers;

import com.example.houda.Exceptions.ResourceNotFoundException;
import com.example.houda.entities.Property;
import com.example.houda.entities.PropertyResponse;
import com.example.houda.repositories.PropertyRepository;
import com.example.houda.services.PropertyService;
import com.example.houda.user.User;
import com.example.houda.user.UserRepository;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository repo;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PropertyService service;

    @GetMapping({"","/"})
    List<Property> showProductList(Model model){
        return repo.findAll();

    }

    @PostMapping("/")
    Property newProperty(@RequestBody Property property){
        return repo.save(property);
    }

    @PostMapping("/CreateProperty")
    public ResponseEntity<?> createNewProperty(@RequestParam("name") String name,
                                               @RequestParam("location") String location,
                                               @RequestParam("nbrBathrooms") Integer nbrBathrooms,
                                               @RequestParam("nbrBedrooms") Integer nbrBedrooms,
                                               @RequestParam("price") Long price,
                                               @RequestParam("surface") Long surface,
                                               @RequestParam("description") String description,
                                               @RequestParam("image") MultipartFile file,
                                               @RequestParam("email") String email) throws IOException {

        User user = userRepo.findByEmail(email).get();
        String uploadImage = service.uploadImageToFileSystem(
                name,
                location,
                nbrBathrooms,
                nbrBedrooms,
                price,
                surface,
                description,
                file,
                user
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @GetMapping("/fileSystem")
    public ResponseEntity<?> showAllProperties() {
        try {
            List<Property> properties = service.getAllProperties();
            List<PropertyResponse> propertyDTOs = new ArrayList<>();

            for (Property property : properties) {
                byte[] imageData = service.downloadImageFromFileSystem(property.getId());

                ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                BufferedImage image = ImageIO.read(bais);
                bais.close();

                int targetWidth = 600; // Set the desired width
                int targetHeight = 400; // Set the desired height
                byte[] compressedImageData = service.compressImage(image, targetWidth, targetHeight);

                // Create PropertyResponse object
                PropertyResponse propertyWithImageDTO = new PropertyResponse();
                propertyWithImageDTO.setId(property.getId());
                propertyWithImageDTO.setName(property.getName());
                propertyWithImageDTO.setLocation(property.getLocation());
                propertyWithImageDTO.setNbrBathrooms(property.getNbrBathrooms());
                propertyWithImageDTO.setNbrBedrooms(property.getNbrBedrooms());
                propertyWithImageDTO.setPrice(property.getPrice());
                propertyWithImageDTO.setSurface(property.getSurface());
                propertyWithImageDTO.setDescription(property.getDescription());
                propertyWithImageDTO.setImagesFileName(property.getImagesFileName());
                propertyWithImageDTO.setImageData(compressedImageData);

                propertyDTOs.add(propertyWithImageDTO);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(propertyDTOs);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading images");
        } catch (PropertyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getproperties")
    public ResponseEntity<?> getProp(@RequestParam String email){
        try{
            User user = userRepo.findByEmail(email).get();
            List<Property> properties=repo.findByUser(user);
            List<PropertyResponse> propertyDTOs = new ArrayList<>();

            for (Property property : properties) {
                byte[] imageData = service.downloadImageFromFileSystem(property.getId());

                ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                BufferedImage image = ImageIO.read(bais);
                bais.close();

                int targetWidth = 600; // Set the desired width
                int targetHeight = 400; // Set the desired height
                byte[] compressedImageData = service.compressImage(image, targetWidth, targetHeight);

                // Create PropertyResponse object
                PropertyResponse propertyWithImageDTO = new PropertyResponse();
                propertyWithImageDTO.setId(property.getId());
                propertyWithImageDTO.setName(property.getName());
                propertyWithImageDTO.setLocation(property.getLocation());
                propertyWithImageDTO.setNbrBathrooms(property.getNbrBathrooms());
                propertyWithImageDTO.setNbrBedrooms(property.getNbrBedrooms());
                propertyWithImageDTO.setPrice(property.getPrice());
                propertyWithImageDTO.setSurface(property.getSurface());
                propertyWithImageDTO.setDescription(property.getDescription());
                propertyWithImageDTO.setImagesFileName(property.getImagesFileName());
                propertyWithImageDTO.setImageData(compressedImageData);

                propertyDTOs.add(propertyWithImageDTO);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(propertyDTOs);
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping("/propertyEdit/{id}")
    public ResponseEntity<?> updateProperty(@RequestBody Property updatedProperty, @PathVariable int id) {
        try {
            Optional<Property> propertyOptional = repo.findById(id);
            if (propertyOptional.isPresent()) {
                Property existingProperty = propertyOptional.get();

                // Update the fields of the existing property
                existingProperty.setName(updatedProperty.getName());
                existingProperty.setLocation(updatedProperty.getLocation());
                existingProperty.setNbrBedrooms(updatedProperty.getNbrBedrooms());
                existingProperty.setNbrBathrooms(updatedProperty.getNbrBathrooms());
                existingProperty.setPrice(updatedProperty.getPrice());
                existingProperty.setSurface(updatedProperty.getSurface());
                existingProperty.setDescription(updatedProperty.getDescription());

                // Save the updated property
                Property savedProperty = repo.save(existingProperty);

                return ResponseEntity.ok(savedProperty);
            } else {
                // Property with the given id not found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle any potential exceptions, such as database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating property: " + e.getMessage());
        }
    }
    @GetMapping("/propertyE/{id}")
    Property getPropertyById(@PathVariable int id){
        return repo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(id));

    }
    @DeleteMapping("/propertyDelete/{id}")
    String deleteProperty(@PathVariable int id){
        if(!repo.existsById(id)){
            throw new ResourceNotFoundException(id);
        }
        repo.deleteById(id);
        return "User with id"+id+"has been deleted success";
    }

}
