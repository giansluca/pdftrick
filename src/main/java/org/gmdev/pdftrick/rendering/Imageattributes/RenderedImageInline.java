package org.gmdev.pdftrick.rendering.Imageattributes;

import org.gmdev.pdftrick.utils.ImageUtils.Flip;

public class RenderedImageInline extends RenderedImageAttributes {
	
	private final int inlineImageCounter;
	private final InlineImage image;
	private final int numPage;
	private final Flip flip;
	private final String rotate;
	private final String key;
	
	public RenderedImageInline(int inlineImageCounter, InlineImage image, int numPage, Flip flip, String rotate) {
		this.inlineImageCounter = inlineImageCounter;
		this.image = image;
		this.numPage = numPage;
		this.flip = flip;
		this.rotate = rotate;
		this.key = numPage + "_" + inlineImageCounter;
	}
	
	@Override
	public int getReference() {
		return inlineImageCounter;
	}
	
	@Override
	public InlineImage getInlineImage() {
		return image;
	}

	@Override
	public int getNumPage() {
		return numPage;
	}

	@Override
	public Flip getFlip() {
		return flip;
	}

	@Override
	public String getRotate() {
		return rotate;
	}

	@Override
	public String getKey() {
		return key;
	}
	


}
