package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.sqlman.model.UpdateResult;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.io.Resources;
import com.luciad.imageio.webp.WebPWriteParam;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

public class TestAvatar extends BaseTest {
    @Test
    void testWriteAvatar() {
        String imagePath = Resources.getResourcesFromClass(TestAvatar.class, "avatar.png");
        File imageFile = new File(imagePath);

        try (FileInputStream inputStream = new FileInputStream(imageFile)) {
            UpdateResult update = new Action("UPDATE user SET avatar_blob = ? WHERE id = 25").update(inputStream).execute();
            System.out.println(update.isOk());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testWriteAvatarWebP() throws IOException {
        String imagePath = Resources.getResourcesFromClass(TestAvatar.class, "avatar.png");

        BufferedImage image = ImageIO.read(new File(imagePath));
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);// 设置有损压缩
        writeParam.setCompressionQuality(0.8f);// 设置 80% 的质量. 设置范围 0-1

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), writeParam);
        ios.close(); // flush to baos

        try (InputStream inputStream = new ByteArrayInputStream(baos.toByteArray())) {
            UpdateResult update = new Action("UPDATE user SET avatar_blob = ? WHERE id = 25").update(inputStream).execute();
            System.out.println(update.isOk());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAvatar() {
        String avatar = new Action("SELECT avatar_blob FROM user WHERE id = 25").query().one(String.class);
        System.out.println(avatar);
    }
}
