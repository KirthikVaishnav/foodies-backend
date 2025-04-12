package com.kirthikv.backend_foodies_app.service;

import com.kirthikv.backend_foodies_app.entity.FoodEntity;
import com.kirthikv.backend_foodies_app.ioobject.FoodRequest;
import com.kirthikv.backend_foodies_app.ioobject.FoodResponse;
import com.kirthikv.backend_foodies_app.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService{
   @Autowired
    private  S3Client s3Client;

   @Autowired
   private FoodRepository foodRepository;

   @Value("${aws.s3.bucketName}")
   private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
      String filenameExtension=  file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
      String key= UUID.randomUUID().toString()+"."+filenameExtension;
      try {
          PutObjectRequest putObjectRequest =PutObjectRequest.builder()
                  .bucket(bucketName)
                  .key(key)
                  .acl("public-read")
                  .contentType(file.getContentType())
                  .build();

          PutObjectResponse response =s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

          if (response.sdkHttpResponse().isSuccessful()){
              return "https://"+bucketName+".s3.amazonaws.com/"+key;
          }
          else {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"AN ERROR OCCURRED WHILE LOADING");
          }
      }catch (IOException exception){
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"AN ERROR OCCURRED WHILE LOADING");
      }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodEntity newFoodEntity= convertToEntity(request);
        String imageUrl=uploadFile(file);
        System.out.println(imageUrl);
        newFoodEntity.setImageUrl(imageUrl);
        newFoodEntity =foodRepository.save(newFoodEntity);
        return convertToResponse(newFoodEntity);
    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> dbEntry = foodRepository.findAll();
        return dbEntry.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFoodsById(String id) {
        FoodEntity existingFood = foodRepository.findById(id).orElseThrow(()->new RuntimeException("Food Not found with id:"+id));
        return convertToResponse(existingFood);
    }

    @Override
    public boolean deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest=DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response =readFoodsById(id);
        String imageUrl = response.getImageUrl();
        String  fileName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isImageDeleted = deleteFile(fileName);
        if (isImageDeleted){
            foodRepository.deleteById(response.getId());
        }
    }

    private FoodEntity convertToEntity(FoodRequest request){
        return FoodEntity.builder()
                .name(request.getName())
                .desc(request.getDesc())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity){
       return FoodResponse.builder()
               .id(entity.getId())
                .name(entity.getName())
                .desc(entity.getDesc())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
