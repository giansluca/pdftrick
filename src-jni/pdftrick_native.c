#include "org_gmdev_pdftrick_nativeutil_NativeLibCall.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <mupdf/fitz.h>

void render(const char*, int, int, char*, int);
char* createLookFile(char *img_path);
void deleteLookFile(char *file_look_path);

JNIEXPORT void JNICALL Java_org_gmdev_pdftrick_nativeutil_NativeLibCall_start
    (JNIEnv *env, jobject obj, jstring resultfile, jstring path, jint nPage, jint zoom){
    
    const char *string_resultfile = (*env)->GetStringUTFChars(env,resultfile,0);
    const char *string_path = (*env)->GetStringUTFChars(env, path,0);
    
    int numberPage = (int) nPage;
    int zoomPic = (int) zoom;
    int rotation = 0;
    
    char *image_ = "image_";
    char *exstension = ".png";
    
    char num[10]; 
    sprintf(num, "%d", numberPage);
    char *img_path = (char*)malloc( (strlen(string_path) + strlen(image_) + strlen(num) + strlen(exstension) +4)*sizeof(char));
    sprintf(img_path, "%s%s%s%s", string_path, image_, num, exstension);
    
    char *look_file = createLookFile(img_path);
    render(string_resultfile, zoomPic, rotation, img_path, numberPage);
    deleteLookFile(look_file);
    free(img_path);
    
    (*env)->ReleaseStringUTFChars(env, path, string_path);
    (*env)->ReleaseStringUTFChars(env, resultfile, string_resultfile);
    
}

JNIEXPORT void JNICALL Java_org_gmdev_pdftrick_nativeutil_NativeLibCall_cover
    (JNIEnv *env, jobject obj, jstring resultfile, jstring path, jint nPage, jint zoom){
    
    const char *string_resultfile = (*env)->GetStringUTFChars(env,resultfile,0);
    const char *string_path = (*env)->GetStringUTFChars(env, path,0);
    
    int numberPage = (int) nPage;
    int zoomPic = (int) zoom;
    int rotation = 0;
    
    char *name_file = "cover_page_";
    char *extension = ".png";
    
    char num[10];
    sprintf(num, "%d", numberPage);
    
    char *img_path = (char*)malloc( (strlen(string_path) + strlen(name_file) + strlen(num) + strlen(extension) +4)*sizeof(char));
    sprintf(img_path, "%s%s%s%s", string_path, name_file, num, extension);
    render(string_resultfile, zoomPic, rotation, img_path, numberPage);
    free(img_path);
    
    (*env)->ReleaseStringUTFChars(env, path, string_path);
    (*env)->ReleaseStringUTFChars(env, resultfile, string_resultfile);
}

/**
 * @param filename
 * @param zoom
 * @param rotation
 * @param img_path
 * @param pagenumber
 */
void render(const char *filename,  int zoom, int rotation, char *img_path, int pagenumber) {
    
	fz_context *ctx;
	fz_document *doc;
	int pagecount;
	fz_page *page;
	fz_matrix transform;
	fz_rect bounds;
	fz_irect bbox;
	fz_pixmap *pix;
	fz_device *dev;

	// Create a context to hold the exception stack and various caches.
	ctx = fz_new_context(NULL, NULL, FZ_STORE_UNLIMITED);

	// Register the default file types.
	fz_register_document_handlers(ctx);

	// Open the PDF, XPS or CBZ document.
	doc = fz_open_document(ctx, filename);

	// Retrieve the number of pages (not used in this example).
	pagecount = fz_count_pages(ctx, doc);

	// Load the page we want. Page numbering starts from zero.
	page = fz_load_page(ctx, doc, pagenumber - 1);

	// Calculate a transform to use when rendering. This transform
	// contains the scale and rotation. Convert zoom percentage to a
	// scaling factor. Without scaling the resolution is 72 dpi.
	fz_rotate(&transform, rotation);
	fz_pre_scale(&transform, zoom / 100.0f, zoom / 100.0f);

	// Take the page bounds and transform them by the same matrix that
	// we will use to render the page.
	fz_bound_page(ctx, page, &bounds);
	fz_transform_rect(&bounds, &transform);

	// Create a blank pixmap to hold the result of rendering. The
	// pixmap bounds used here are the same as the transformed page
	// bounds, so it will contain the entire page. The page coordinate
	// space has the origin at the top left corner and the x axis
	// extends to the right and the y axis extends down.
	fz_round_rect(&bbox, &bounds);
	pix = fz_new_pixmap_with_bbox(ctx, fz_device_rgb(ctx), &bbox);
	fz_clear_pixmap_with_value(ctx, pix, 0xff);

	// A page consists of a series of objects (text, line art, images,
	// gradients). These objects are passed to a device when the
	// interpreter runs the page. There are several devices, used for
	// different purposes:
	//
	//	draw device -- renders objects to a target pixmap.
	//
	//	text device -- extracts the text in reading order with styling
	//	information. This text can be used to provide text search.
	//
	//	list device -- records the graphic objects in a list that can
	//	be played back through another device. This is useful if you
	//	need to run the same page through multiple devices, without
	//	the overhead of parsing the page each time.

	// Create a draw device with the pixmap as its target.
	// Run the page with the transform.
	dev = fz_new_draw_device(ctx, pix);
	fz_run_page(ctx, page, dev, &transform, NULL);
	fz_drop_device(ctx, dev);

	// Save the pixmap to a file.
	fz_write_png(ctx, pix, img_path, 0);

	// Clean up.
	fz_drop_pixmap(ctx, pix);
	fz_drop_page(ctx, page);
	fz_drop_document(ctx, doc);
	fz_drop_context(ctx);
}

/**
 * @param char*
 * @return char* 
 */
char* createLookFile(char *img_path) {
    char *look = ".look";
    char *file_look_path = (char*)malloc( (strlen(img_path) + strlen(look) +4)*sizeof(char));
    sprintf(file_look_path ,"%s%s",img_path,look);
    
    FILE *f = fopen(file_look_path, "w");
    if (f == NULL) {
        return NULL;
    }
    fclose(f);
    return file_look_path;
}

/**
 * @param char*
 */
void deleteLookFile(char *file_look_path) {
    remove (file_look_path);
    free(file_look_path);
}





