package org.gmdev.pdftrick.rendering.Imageattributes;

import org.gmdev.pdftrick.utils.ImageUtils.Flip;

public abstract class RenderedImageAttributes {
	
	public abstract InlineImage getInlineImage();
	
	public abstract int getReference();
	
	public abstract int getNumPage();
	
	public abstract Flip getFlip();
	
	public abstract String getRotate();
	
	public abstract String getKey();
	
	
}
