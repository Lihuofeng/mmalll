package com.bees360.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bees360.common.Const;
import com.bees360.common.ResponseCode;
import com.bees360.common.ServiceResponse;
import com.bees360.pojo.Product;
import com.bees360.pojo.User;
import com.bees360.service.FileService;
import com.bees360.service.ProductService;
import com.bees360.service.UserService;
import com.bees360.utils.PropertiesUtil;
import com.bees360.vo.ProductDetailVo;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("manage/product")
public class ProductManageController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 新增商品或修改商品
	 */
	@RequestMapping("save.do")
	@ResponseBody
	public ServiceResponse productSave(HttpSession session,Product product) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			//添加产品
			return productService.saveOrUpdateProduct(product);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}

	}

	/**
	 * 修改产品销售状态
	 * @param session
	 * @param productId
	 * @param status
	 * @return
	 */

	@RequestMapping("set_sale_status.do")
	@ResponseBody
	public ServiceResponse setSaleStatus(HttpSession session,Integer productId,Integer status) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return productService.setSaleStatus(productId, status);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}

	}

	/**
	 * 获取商品详情
	 * @param session
	 * @param productId
	 * @return
	 */

	@RequestMapping("detail.do")
	@ResponseBody
	public ServiceResponse getDetail(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return productService.manageProductDetail(productId);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}

	}

	/**
	 * 后台展示商品列表
	 * @param session
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServiceResponse getList(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value="pageSize",defaultValue="10")int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return productService.getProductList(pageNum, pageSize);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}

	}
	
	/**
	 * 后台产品搜索
	 * @param session
	 * @return
	 */
	@RequestMapping("search.do")
	@ResponseBody
	public ServiceResponse productSearch(HttpSession session,String productName,Integer productId,@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value="pageSize",defaultValue="10")int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			return productService.searchProduct(productName, productId, pageNum, pageSize);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}

	}
	
	/**
	 * 图片上传
	 * @param session
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("upload.do")
	@ResponseBody
	public ServiceResponse upload(HttpSession session,@RequestParam(value="upload_file",required=false)MultipartFile file,HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName=fileService.upload(file, path);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			Map  fileMap=Maps.newHashMap();
			fileMap.put("uri", targetFileName);
			fileMap.put("url", url);
			return ServiceResponse.createBySuccess(fileMap);
		}else {
			return ServiceResponse.createByErrorMessage("无权限操作");
		}

	}
	
	
	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public Map richtextImgUpload(HttpSession session,@RequestParam(value="upload_file",required=false)MultipartFile file,HttpServletRequest request,HttpServletResponse response) {
		Map resultMap =Maps.newHashMap();
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			resultMap.put("success", false);
			resultMap.put("msg", "用户未登录，请登录");
			return resultMap;
		}
		if(userService.chekAdminRole(user).isSuccess()) {
			//使用simditor,按照其返回
			
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName=fileService.upload(file, path);
			if(StringUtils.isBlank(targetFileName)) {
				resultMap.put("success", false);
				resultMap.put("msg", "上传失败");
				return resultMap;
			}
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			resultMap.put("success", true);
			resultMap.put("msg", "上传成功");
			resultMap.put("file_path", url);
			response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
			return resultMap;
		}else {
			resultMap.put("success", false);
			resultMap.put("msg", "无权限操作");
			return resultMap;
		}

	}

}
