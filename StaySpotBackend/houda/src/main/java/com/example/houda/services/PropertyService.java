package com.example.houda.services;

import com.example.houda.entities.Property;
import com.example.houda.repositories.PropertyRepository;
import com.example.houda.user.User;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
@Service
public class PropertyService {

    @Autowired
    private PropertyRepository repo;
    @Autowired
    private final String FOLDER_PATH="C:/Users/pro/Documents/Projet_SpringBoot/images/";

    public String uploadImageToFileSystem( String name,
                                           String location,
                                           Integer nbrBathrooms,
                                           Integer nbrBedrooms,
                                           Long price,
                                           Long surface,
                                           String description,
                                           MultipartFile file,
                                           User user) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        Property fileData=repo.save(Property.builder()
                .name(name)
                .location(location)
                .nbrBathrooms(nbrBathrooms)
                .nbrBedrooms(nbrBedrooms)
                .price(price)
                .surface(surface)
                .description(description)
                .user(user)
                .imagesFileName(filePath).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }

    public Property getPropertyById(int id) throws PropertyNotFoundException {
        Optional<Property> propertyOptional = repo.findById(id);
        if (propertyOptional.isPresent()) {
            return propertyOptional.get();
        } else {
            throw new PropertyNotFoundException("Property with id " + id + " not found");
        }
    }

    public byte[] downloadImageFromFileSystem(int propertyId) throws IOException {
        Optional<Property> propertyOptional = repo.findById(propertyId);

        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            String filePath = property.getImagesFileName(); // Get image file path from Property entity
            return Files.readAllBytes(new File(filePath).toPath());
        } else {
            throw new PropertyNotFoundException("Property with id " + propertyId + " not found");
        }
    }

    public List<Property> getAllProperties() {
        return repo.findAll();
    }

    public byte[] compressImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", outputStream);

        return outputStream.toByteArray();
    }
}
