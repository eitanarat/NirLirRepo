package com.nirlir.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DetectLogosService {

    // Detects logos in the specified local image.
    private final Logger log = LoggerFactory.getLogger(DetectLogosService.class);

    public void detectLogos(byte[] logo) throws IOException {
        List<AnnotateImageRequest> requests = buildRequests(logo);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    log.error("Error: %s%n", res.getError().getMessage());
                    throw new RuntimeException(res.getError().getMessage());
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
                    log.debug(annotation.getDescription());
                }
            }
        }
    }

    private static List<AnnotateImageRequest> buildRequests(byte[] logo) {
        ByteString imageBytes = ByteString.copyFrom(logo);
        Image img = Image.newBuilder().setContent(imageBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LOGO_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(request);
        return requests;
    }
}
