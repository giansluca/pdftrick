package org.gmdev.pdftrick.engine.ImageAttr;

public class RenderedImageInline extends RenderedImageAttributes {
	
	private final int inlineImageCounter;
	private final InlineImage image;
	private final int numPage;
	private final String flip;
	private final String rotate;
	private final String key;
	
	public RenderedImageInline(int inlineImageCounter, InlineImage image, int numPage, String flip, String rotate) {
		this.inlineImageCounter = inlineImageCounter;
		this.image = image;
		this.numPage = numPage;
		this.flip = flip;
		this.rotate = flip;
		this.key = numPage+"_"+inlineImageCounter;
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
	public String getFlip() {
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
