package com.egg.common.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.egg.common.log.LogKit;

/**
 * 图片工具
 */
public class ImageUtil {

	public static final String[] IMG_EXTS = { ".jpg", ".png", ".gif", ".bmp" };

	public static boolean isImage(String filename) {
		if (StringUtils.isBlank(filename)) {
			return false;
		}

		for (String ext : IMG_EXTS) {
			if (filename.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 裁剪图片。取最小边截取正方形，缩小到指定值
	 */
	public static boolean cropSquare(InputStream in, String filepath, int zoomValue) {
		if (in == null) {
			return false;
		}
		try {
			if (zoomValue <= 0) {
				return false;
			}

			BufferedImage img = ImageIO.read(in);
			if (img == null) {
				return false;
			}

			int cropValue = Math.min(img.getWidth(), img.getHeight());
			zoomValue = Math.min(cropValue, zoomValue);

			// 截图
			BufferedImage cropImg = img.getSubimage(0, 0, cropValue, cropValue);

			// 保存图片
			return saveScaleImage(cropImg, filepath, zoomValue, zoomValue);
		} catch (Exception e) {
			LogKit.error(".cropSquare() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(in);
		}
	}

	/**
	 * 裁剪图片，并按比例缩放到指定大小
	 */
	public static boolean crop(InputStream in, String filepath, int x1, int y1, int w, int h, int nW, int nH) {
		if (in == null) {
			return false;
		}
		try {
			BufferedImage img = ImageIO.read(in);
			return crop(img, filepath, x1, y1, w, h, nW, nH);
		} catch (Exception e) {
			LogKit.error(".crop() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(in);
		}
	}

	/**
	 * 裁剪图片，并按比例缩放到指定大小
	 */
	public static boolean crop(BufferedImage img, String filepath, int x1, int y1, int w, int h, int nW, int nH) {
		if (img == null) {
			return false;
		}
		if (w <= 0 || h <= 0) {
			return false;
		}
		if (nW <= 0 || nH <= 0) {
			return false;
		}
		try {
			x1 = Math.max(0, x1);
			y1 = Math.max(0, y1);
			w = Math.min(w, img.getWidth());
			h = Math.min(h, img.getHeight());

			double ar = Math.min((nW * 1D) / w, (nH * 1D) / h);
			nW = (int) (w * ar);
			nH = (int) (h * ar);

			// 截图
			BufferedImage cropImg = img.getSubimage(x1, y1, w, h);

			// 保存图片
			return saveScaleImage(cropImg, filepath, nW, nH);
		} catch (Exception e) {
			LogKit.error(".crop() : 方法出现异常 : ", e);
			return false;
		}
	}

	/**
	 * 按比例缩放到指定大小
	 */
	public static boolean zoom(String filepath, String savepath, int width, int height) {
		InputStream in = null;
		try {
			in = new FileInputStream(filepath);
			return zoom(in, savepath, width, height);
		} catch (Exception e) {
			LogKit.error(".zoom() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(in);
		}
	}

	/**
	 * 按比例缩放到指定大小
	 */
	public static boolean zoom(InputStream in, String filepath, int width, int height) {
		if (in == null) {
			return false;
		}
		try {
			BufferedImage img = ImageIO.read(in);
			return zoom(img, filepath, width, height, false);
		} catch (Exception e) {
			LogKit.error(".zoom() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(in);
		}
	}

	/**
	 * 缩放到指定宽高
	 */
	public static boolean zoomFix(InputStream in, String filepath, int width, int height) {
		if (in == null) {
			return false;
		}
		try {
			BufferedImage img = ImageIO.read(in);
			return zoom(img, filepath, width, height, true);
		} catch (Exception e) {
			LogKit.error(".zoomFix() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(in);
		}
	}

	/**
	 * 按比例缩放到指定大小
	 */
	public static boolean zoom(BufferedImage img, String filepath, int width, int height, boolean isFixed) {
		if (img == null) {
			return false;
		}
		if (width <= 0 || height <= 0) {
			return false;
		}
		try {
			if (!isFixed) {
				// 得到宽高
				int w = img.getWidth();
				int h = img.getHeight();
				if (w <= width && h <= height) {
					width = w;
					height = h;
				} else {
					double ar = Math.min((width * 1D) / w, (height * 1D) / h);
					width = (int) (w * ar);
					height = (int) (h * ar);
				}
			}

			// 保存图片
			return saveScaleImage(img, filepath, width, height);
		} catch (Exception e) {
			LogKit.error(".zoom() : 方法出现异常 : ", e);
			return false;
		}
	}

	public static boolean enlarge(String filePath, String savePath, int min) {
		InputStream in = null;
		try {
			in = new FileInputStream(filePath);
			return enlarge(in, savePath, min);
		} catch (Exception e) {
			LogKit.error(".enlarge() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(in);
		}
	}

	public static boolean enlarge(InputStream in, String savePath, int min) {
		if (in == null) {
			return false;
		}
		try {
			BufferedImage img = ImageIO.read(in);
			int w = img.getWidth();
			int h = img.getHeight();
			int toWidth = w;
			int toHeight = h;
			if (w < min || h < min) {
				if (w < h) {
					toWidth = min;
					toHeight = (int) (h * 1.0 * min / w);
				} else {
					toHeight = min;
					toWidth = (int) (w * 1.0 * min / h);
				}
			}
			// 保存图片
			return saveScaleImage(img, savePath, toWidth, toHeight);
		} catch (Exception e) {
			LogKit.error(".enlarge() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(in);
		}
	}

	/**
	 * 得到扩展名：.jpg, .png 等
	 */
	public static String ext(String filename) {
		if (filename == null || filename.isEmpty()) {
			return "";
		}
		int ind = filename.lastIndexOf(".");
		if (ind == -1) {
			return "";
		}
		return filename.substring(ind).toLowerCase();
	}

	/**
	 * 将图片保存为PNG格式
	 */
	public static boolean savePNG(InputStream in, String filepath) {
		if (in == null) {
			return false;
		}
		OutputStream out = null;
		try {
			BufferedImage img = ImageIO.read(in);
			return saveScaleImage(img, filepath, img.getWidth(), img.getHeight());
		} catch (Exception e) {
			LogKit.error(".savePNG() : 方法出现异常 : ", e);
			return false;
		} finally {
			FileUtil.quicklyClose(out);
			FileUtil.quicklyClose(in);
		}
	}

	// ==============
	// private method
	// ==============
	/**
	 * 保存缩放图片
	 * 
	 * @param image 原图
	 * @param file 保存
	 * @param w 宽
	 * @param h 高
	 * @throws IOException
	 */
	private static boolean saveScaleImage(BufferedImage image, String filepath, int w, int h) throws IOException {
		if (image == null) {
			return false;
		}

		if (StringUtils.isBlank(filepath)) {
			return false;
		}
		File file = new File(filepath);
		if (!FileUtil.mkdirs(file)) {
			return false;
		}

		// formatName
		String formatName = getFormatName(file.getName()).toLowerCase();
		if (!"jpg".equals(formatName) && !"png".equals(formatName)) {
			formatName = "jpg";
		}

		// 创建图片
		BufferedImage taget = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) taget.getGraphics();
		// PNG图片这是透明
		if ("png".equals(formatName)) {
			taget = g2d.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			g2d.dispose();
			g2d = taget.createGraphics();
		}
		g2d.drawImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);

		// 保存图片
		return ImageIO.write(taget, formatName, file);
	}

	private static String getFormatName(String filename) {
		try {
			int ind = filename.lastIndexOf(".");
			return filename.substring(ind + 1).toLowerCase();
		} catch (Exception e) {
			return "";
		}
	}

}
