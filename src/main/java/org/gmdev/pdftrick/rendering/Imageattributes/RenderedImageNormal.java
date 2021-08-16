package org.gmdev.pdftrick.rendering.Imageattributes;

import org.gmdev.pdftrick.utils.ImageUtils.Flip;

public class RenderedImageNormal extends RenderedImageAttributes {
	
	private final int imageRefNumber;
	private final int numPage;
	private final Flip flip;
	private final String rotate;
	private final String key;
	
	public RenderedImageNormal(int numPage, int imageRefNumber, Flip flip, String rotate) {
		this.imageRefNumber = imageRefNumber;
		this.numPage = numPage;
		this.flip = flip;
		this.rotate = rotate;
		this.key = numPage + "_" + imageRefNumber;
	}
	
	@Override
	public int getReference() {
		return imageRefNumber;
	}
	
	@Override
	public InlineImage getInlineImage() {
		return null;
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
