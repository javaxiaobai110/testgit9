package com.bookStore.admin.product.handler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bookStore.admin.product.service.IAdminProductService;
import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.ProductList;
import com.bookStore.utils.IdUtils;
import com.bookStore.utils.PageModel;

@Controller
@RequestMapping("/admin/products")
public class AdminProductHandler {
	
	@Autowired
	private IAdminProductService adminProductService;
	
	@RequestMapping("/listProduct.do")
	public String listProduct(Model model,@RequestParam(defaultValue="1")Integer pageIndex){
		PageModel pageModel = new PageModel();
		pageModel.setPageIndex(pageIndex);
		
		int count = adminProductService.findProductsCount();
		pageModel.setRecordCount(count);
		
		List<Product> products = adminProductService.findProduct();
		model.addAttribute("products", products);
		model.addAttribute("pageModel", pageModel);
		
		return "/admin/products/list.jsp";
	}
	
	@RequestMapping("/findProductByManyCondition.do")
	public String findProductByManyCondition(Product product, String minprice, String maxprice, Model model){
		/*PageModel pageModel = new PageModel();
		pageModel.setPageIndex(pageIndex);
		int count = adminProductService.findProductByConditionCount(product,minprice,maxprice);
		pageModel.setRecordCount(count);*/
		
		List<Product> products = adminProductService.findProductByManyCondition(product, minprice, maxprice);
		model.addAttribute("products", products);
		model.addAttribute("product", product);
		model.addAttribute("minprice", minprice);
		model.addAttribute("maxprice", maxprice);
		/*model.addAttribute("pageModel", pageModel);*/
		
		return "/admin/products/list.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct(MultipartFile upload, Product product, HttpServletRequest request) throws IllegalStateException, IOException{
		
		String path = request.getSession().getServletContext().getRealPath("/productImg");
		File file = new File(path);
		if(!file.exists()){        //file.exists(), 如果路径存在返回true
			file.mkdirs();
		}
		String filename = IdUtils.getUUID() + "-" +upload.getOriginalFilename();
		String imgurl = path + File.separatorChar + filename;
		upload.transferTo(new File(imgurl));
		product.setId(IdUtils.getUUID());
		product.setImgurl("/productImg/" + filename);
		
		int count = adminProductService.addProduct(product);
		return "/admin/products/listProduct.do";
		
	}
	
	@RequestMapping("/findProductById.do")
	public String findProductById(String id, Model model){
		Product product = adminProductService.findProductById(id);
		
		model.addAttribute("p", product);
		
		return "/admin/products/edit.jsp";
		
	}
	
	@RequestMapping("/editProduct.do")
	public String editProduct(Product product, MultipartFile upload, HttpSession session) throws IllegalStateException, IOException{
		if(!upload.isEmpty()){
			String path = session.getServletContext().getRealPath("/productImg");
			Product target = adminProductService.findProductById(product.getId());
			File targetfile = new File(session.getServletContext().getRealPath("/") + target.getImgurl());
			if(targetfile.exists()){
				targetfile.delete();
			}
			String filename = IdUtils.getUUID() + "-" + upload.getOriginalFilename();
			String imgurl = path + File.separatorChar + filename;
			upload.transferTo(new File(imgurl));
			product.setImgurl("/productImg/" + filename);
		}
		int count = adminProductService.modifyProduct(product);
		return "/admin/products/listProduct.do";
	}
	
	@RequestMapping("/deleteProduct.do")
	public String deleteProduct(String id, HttpSession session){
		Product product = adminProductService.findProductById(id);
	    File targetfile = new File(session.getServletContext().getRealPath("/") + product.getImgurl());
	    if(targetfile.exists()){
	    	targetfile.delete();
	    }
	    int count = adminProductService.removeProduct(id);
	    return "/admin/products/listProduct.do";
	}
	
	@RequestMapping("/download.do")
	public void download(String year, String month, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws IOException{
		
		List<ProductList> plist = adminProductService.findProductList(year, month);
		String filename = year +"年" + month + "月销售榜单.csv";
		//为防止文件名乱码，可以进行编码回退
		response.setHeader("Content-Disposition", "attachment;filename="+processFileName(request, filename));
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gbk"),"ISO-8859-1"));  
		response.setContentType(session.getServletContext().getMimeType(filename));
		response.setCharacterEncoding("GBK");
		
		
		PrintWriter out = response.getWriter();
	    out.println("商品名称,商品销量");
	    for(int i=0; i<plist.size(); i++){
	    	ProductList pl = plist.get(i);
	    	out.println(pl.getName() + "," + pl.getSalnum());
	    }
	    out.flush();
	    out.close();
	}

	
	
	
	
	
	public String processFileName(HttpServletRequest request, String fileNames){
		String codedFilename = null;
		try{
			String agent = request.getHeader("USER-AGENT");
			if(null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 !=agent.indexOf("Trident")){  //IE
				String name = java.net.URLEncoder.encode(fileNames, "UTF-8");
				
				codedFilename = name;
			}else if(null != agent && -1 != agent.indexOf("Mozilla")){  //火狐，Chrome等
				codedFilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return codedFilename;
	}
	
	
	
	
	
	
	
	
	
}
