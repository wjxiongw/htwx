package com.wy.wx.config;


import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import org.webjars.WebJarAssetLocator;

import springfox.documentation.annotations.ApiIgnore;

/**
 *当有新版本的web资源的时候，当然不希望一个个的去客户端修改资源版本号，我们利用WebJarAssetLocator来处理
 * 在页面上，就这么调用，就不需要写具体版本号
 */
@ApiIgnore  //忽略在swagger中显示
@RestController
public class WebJarsController {
    
    private final WebJarAssetLocator assetLocator = new WebJarAssetLocator();
    
    @RequestMapping("/webjarslocator/{webjar}/**")
    public ResponseEntity<Object> locateWebjarAsset(@PathVariable String webjar, HttpServletRequest request) {
        try {
            // This prefix must match the mapping path!
            String mvcPrefix = "/webjarslocator/" + webjar + "/";
            String mvcPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String fullPath = assetLocator.getFullPath(webjar, mvcPath.substring(mvcPrefix.length()));
            return new ResponseEntity<>(new ClassPathResource(fullPath), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}