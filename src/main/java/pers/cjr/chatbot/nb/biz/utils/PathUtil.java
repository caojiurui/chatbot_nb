package pers.cjr.chatbot.nb.biz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PathUtil {
    private static Logger log = LoggerFactory.getLogger(PathUtil.class);

    private static PathUtil pathUtil;
    @Value("${web.domain.upload}")
    private String UPLOAD_BASE_URL;

    @Value("${web.updateDir}")
    private String updateDir;

    @PostConstruct
    public void init(){
        pathUtil = this;
        pathUtil.updateDir = updateDir;
    }

    public static String getExampaperOriginalImageUrl(Long exampaperId) {
        return pathUtil.UPLOAD_BASE_URL + "/exampaper/pdf_img/" + exampaperId + "/original.png";
    }

    public static String getExampaperThumbnailImageUrl(Long exampaperId) {
        return pathUtil.UPLOAD_BASE_URL + "/exampaper/pdf_img/" + exampaperId + "/thumbnail.png";
    }

    public static String getExampaperRectDataFilePath(String exampaperId) {
        return pathUtil.updateDir + "/exampaper/rect_data/" + exampaperId + "_rect.dat";
    }

    public static String getOcrCnNoLineXmlPath() {
        return pathUtil.updateDir + "/exampaper/ocr/CN_NO_LINE.xml";
    }
}
