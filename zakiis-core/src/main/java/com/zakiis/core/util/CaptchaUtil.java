package com.zakiis.core.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class CaptchaUtil {
	
	final static int DEFAULT_WIDTH = 60;
	final static int DEFAULT_HEIGHT = 20;
	final static int DEFAULT_CAPTCHA_LENGTH = 4;

	//https://www.jianshu.com/p/009914797af2
	public static String genImgCode(int width, int height, int captchaLength, OutputStream os) throws IOException {
		if (width <= 0) {
			width = DEFAULT_WIDTH;
		}
		if (height <= 0) {
			height = DEFAULT_HEIGHT;
		}
		if (captchaLength <= 0) {
			captchaLength = DEFAULT_CAPTCHA_LENGTH;
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		Color backColor = drawFrame(g2, width, height);
		drawNoise(g2, image, width, height);
		shear(g2, width, height, backColor);
		drawCode(g2, width, height, captchaLength);

		g2.dispose();
		ImageIO.write(image, "png", os);
		return null;
	}

	private static String drawCode(Graphics2D g2, int width, int height, int captchaLength) {
		String captchaCode = RandomStringUtils.randomAlphabetic(captchaLength);
		char[] chars = captchaCode.toCharArray();
		g2.setColor(getRandomColor(100, 160));
		int fontSize = height - 4;
		Font font = new Font("Algerian", Font.ITALIC, fontSize);
		g2.setFont(font);
		for (int i = 0; i < chars.length; i++) {
			AffineTransform transform = new AffineTransform();
			//Increasing the value of first parameter will add difficulties of the captcha.
			transform.setToRotation(Math.PI / RandomUtils.nextDouble(4.5, 7.5) * (RandomUtils.nextBoolean() ? 1 : -1), (width / chars.length) * i + fontSize / 2, height / 2);
			g2.setTransform(transform);
			g2.drawChars(chars, i, 1, (width - 10) / chars.length * i + 5, height / 2 + fontSize / 2 - 5);
		}
		return captchaCode;
		
	}

	private static void shear(Graphics2D g2, int width, int height, Color backColor) {
		//扭曲图像
		shearX(g2, width, height, backColor);
		shearY(g2, width, height, backColor);
		
	}

	private static void shearY(Graphics2D g2, int width, int height, Color backColor) {
		int period = RandomUtils.nextInt(0, 40);
		int frames = 20;
		int phase = 7;
		boolean borderGap = true;
		for (int i = 0; i < width; i++) {
			double d = (double) (period >> 1) * Math.sin((double)i / (double)period
					+ (6.2831853071795862D * (double) phase) / (double) frames);
			g2.copyArea(i, 0, 1, height, 0, (int) d);
			if (borderGap) {
				g2.setColor(backColor);
				g2.drawLine(i, (int)d, i, 0);
				g2.drawLine(i, (int)d + height, i, height);
			}
		}
	}

	private static void shearX(Graphics2D g2, int width, int height, Color backColor) {
		int period = RandomUtils.nextInt(0, 2);
		int frames = 1;
		int phase = RandomUtils.nextInt(0, 2);
		boolean borderGap = true;
		for (int i = 0; i < height; i++) {
			double d = (double) (period >> 1) * Math.sin((double)i / (double)period
					+ (6.2831853071795862D * (double) phase) / (double) frames);
			g2.copyArea(0, i, width, 1, (int) d, 0);
			if (borderGap) {
				g2.setColor(backColor);
				g2.drawLine((int)d, i, 0, i);
				g2.drawLine((int)d + width, i, width, i);
			}
		}
	}

	private static void drawNoise(Graphics2D g2, BufferedImage image, int width, int height) {
		//draw noise line
		g2.setColor(getRandomColor(160, 200));
		for (int i = 0; i < 20; i++) {
			int x = RandomUtils.nextInt(1, width - 1);
			int y = RandomUtils.nextInt(1, height - 1);
			int x1 = RandomUtils.nextInt(x, width - 1);
			int y1 = RandomUtils.nextInt(y, height - 1);
			g2.drawLine(x, y, x1, y1);
		}
		//draw noise point
		float noiseRate = 0.05f;
		int area = (int) (width * height * noiseRate);
		for (int i = 0; i < area; i++) {
			int x = RandomUtils.nextInt(1, width - 1);
			int y = RandomUtils.nextInt(1, height - 1);
			image.setRGB(x, y, getRandomIntColor(0, 256));
		}
	}

	private static Color drawFrame(Graphics2D g2, int width, int height) {
		//draw border
		g2.setColor(Color.gray);
		g2.fillRect(0, 0, width, height);
		//draw background
		Color backColor = getRandomColor(200, 250);
		g2.setColor(backColor);
		g2.fillRect(1, 1, width - 2, height - 2);
		return backColor;
	}
	
	private static Color getRandomColor(int startInclusive, int endExclusive) {
		int r = RandomUtils.nextInt(startInclusive, endExclusive);
		int g = RandomUtils.nextInt(startInclusive, endExclusive);
		int b = RandomUtils.nextInt(startInclusive, endExclusive);
		return new Color(r, g, b);
	}
	
	private static int getRandomIntColor(int startInclusive, int endExclusive) {
		Color color = getRandomColor(startInclusive, endExclusive);
		int rgb = color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
		return rgb;
	}
}
