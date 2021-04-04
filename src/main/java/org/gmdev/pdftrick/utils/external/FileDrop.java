package org.gmdev.pdftrick.utils.external;

import java.awt.*;
import java.awt.datatransfer.*;

import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * This class makes it easy to drag and drop files from the operating
 * system to a Java program. Any <tt>java.awt.Component</tt> can be
 * dropped onto, but only <tt>javax.swing.JComponent</tt>s will indicate
 * the drop event with a changed border.
 * <p/>
 * To use this class, construct a new <tt>FileDrop</tt> by passing
 * it the target component and a <tt>Listener</tt> to receive notification
 * when file(s) have been dropped. Here is an example:
 * <p/>
 * <code><pre>
 *      JPanel myPanel = new JPanel();
 *      new FileDrop( myPanel, new FileDrop.Listener()
 *      {   public void filesDropped( java.io.File[] files )
 *          {   
 *              // handle file drop
 *              ...
 *          }   // end filesDropped
 *      }); // end FileDrop.Listener
 * </pre></code>
 * <p/>
 * You can specify the border that will appear when files are being dragged by
 * calling the constructor with a <tt>javax.swing.border.Border</tt>. Only
 * <tt>JComponent</tt>s will show any indication with a border.
 * <p/>
 * You can turn on some debugging features by passing a <tt>PrintStream</tt>
 * object (such as <tt>System.out</tt>) into the full constructor. A <tt>null</tt>
 * value will result in no extra debugging information being output.
 * <p/>
 *
 * <p>I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p><em>Original author: Robert Harder, rharder@usa.net</em></p>
 * <p>2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.</p>
 *
 * @author  Robert Harder
 * @author  rharder@users.sf.net
 * @version 1.0.1
 */
public class FileDrop {
    private transient Border normalBorder;
    private transient DropTargetListener dropListener;
    private static Boolean supportsDnD;
    private static final Color defaultBorderColor = new Color( 0f, 0f, 1f, 0.25f );
    
    /**
     * Constructs a {@link FileDrop} with a default light-blue border
     * and, if <var>c</var> is a {@link Container}, recursively
     * sets all elements contained within as drop targets, though only
     * the top level container will change borders.
     *
     * @param c Component on which files will be dropped.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final Component c, final Listener listener) {   
    	this (null, 
    			c,
    			BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor),
    			true,
    			listener);
    }
    
    /**
     * Constructor with a default border and the option to recursively set drop targets.
     * If your component is a <tt>java.awt.Container</tt>, then each of its children
     * components will also listen for drops, though only the parent will change borders.
     *
     * @param c Component on which files will be dropped.
     * @param recursive Recursively set children as drop targets.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final Component c, final boolean recursive, final Listener listener) {
    	this(null,
    			c,
    			BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor),
    			recursive,
    			listener );
    } 
    
    /**
     * Constructor with a default border and debugging optionally turned on.
     * With Debugging turned on, more status messages will be displayed to
     * <tt>out</tt>. A common way to use this constructor is with
     * <tt>System.out</tt> or <tt>System.err</tt>. A <tt>null</tt> value for
     * the parameter <tt>out</tt> will result in no debugging output.
     *
     * @param out PrintStream to record debugging info or null for no debugging.
     * @param out 
     * @param c Component on which files will be dropped.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final PrintStream out, final Component c, final Listener listener) {
    	this(out,
    			c,
    			BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor), 
    			false,
    			listener);
    }
    
    /**
     * Constructor with a default border, debugging optionally turned on
     * and the option to recursively set drop targets.
     * If your component is a <tt>java.awt.Container</tt>, then each of its children
     * components will also listen for drops, though only the parent will change borders.
     * With Debugging turned on, more status messages will be displayed to
     * <tt>out</tt>. A common way to use this constructor is with
     * <tt>System.out</tt> or <tt>System.err</tt>. A <tt>null</tt> value for
     * the parameter <tt>out</tt> will result in no debugging output.
     *
     * @param out PrintStream to record debugging info or null for no debugging.
     * @param out 
     * @param c Component on which files will be dropped.
     * @param recursive Recursively set children as drop targets.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final PrintStream out, final Component c, final boolean recursive, final Listener listener) {   
    	this(out,
              c,
              BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor ),
              recursive,
              listener);
    } 
    
    /**
     * Constructor with a specified border 
     *
     * @param c Component on which files will be dropped.
     * @param dragBorder Border to use on <tt>JComponent</tt> when dragging occurs.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final Component c, final Border dragBorder, final Listener listener) {
    	this(null,
            c,
            dragBorder,
            false,
            listener);
    } 
    
    /**
     * Constructor with a specified border and the option to recursively set drop targets.
     * If your component is a <tt>java.awt.Container</tt>, then each of its children
     * components will also listen for drops, though only the parent will change borders.
     *
     * @param c Component on which files will be dropped.
     * @param dragBorder Border to use on <tt>JComponent</tt> when dragging occurs.
     * @param recursive Recursively set children as drop targets.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final Component c, final Border dragBorder, final boolean recursive, final Listener listener) {
    	this(null,
            c,
            dragBorder,
            recursive,
            listener );
    }
    
    /**
     * Constructor with a specified border and debugging optionally turned on.
     * With Debugging turned on, more status messages will be displayed to
     * <tt>out</tt>. A common way to use this constructor is with
     * <tt>System.out</tt> or <tt>System.err</tt>. A <tt>null</tt> value for
     * the parameter <tt>out</tt> will result in no debugging output.
     *
     * @param out PrintStream to record debugging info or null for no debugging.
     * @param c Component on which files will be dropped.
     * @param dragBorder Border to use on <tt>JComponent</tt> when dragging occurs.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final PrintStream out, final Component c, final Border dragBorder, final Listener listener) {
    	this(
            out,
            c,
            dragBorder,
            false,
            listener);
    }
    
    /**
     * Full constructor with a specified border and debugging optionally turned on.
     * With Debugging turned on, more status messages will be displayed to
     * <tt>out</tt>. A common way to use this constructor is with
     * <tt>System.out</tt> or <tt>System.err</tt>. A <tt>null</tt> value for
     * the parameter <tt>out</tt> will result in no debugging output.
     *
     * @param out PrintStream to record debugging info or null for no debugging.
     * @param c Component on which files will be dropped.
     * @param dragBorder Border to use on <tt>JComponent</tt> when dragging occurs.
     * @param recursive Recursively set children as drop targets.
     * @param listener Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(
    		final PrintStream out,
    		final Component c,
    		final Border dragBorder,
    		final boolean recursive,
    		final Listener listener) {

    	if(supportsDnD()) {
    		dropListener = new DropTargetListener() {
    			public void dragEnter(DropTargetDragEvent evt ) {      
    				log(out, "FileDrop: dragEnter event." ); 

    				if(isDragOk(out, evt)) {    
    					if(c instanceof JComponent) {   
    						JComponent jc = (JComponent) c;
    						normalBorder = jc.getBorder();
    						log( out, "FileDrop: normal border saved." );
    						jc.setBorder( dragBorder );
    						log( out, "FileDrop: drag border set." );
    					}                          
    					evt.acceptDrag(DnDConstants.ACTION_COPY );
    					log( out, "FileDrop: event accepted." );
    				}
    				else {
    					evt.rejectDrag();
    					log( out, "FileDrop: event rejected." );
    				}
    			}

    			public void dragOver(DropTargetDragEvent evt ) {
    			}

    			public void drop(DropTargetDropEvent evt ) {
    				log( out, "FileDrop: drop event." );
    				try {
    					Transferable tr = evt.getTransferable();
    					if (tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor)) {
    						evt.acceptDrop (DnDConstants.ACTION_COPY);
    						log( out, "FileDrop: file list accepted.");

    						List<?> fileList =
    								(List<?>) tr.getTransferData(DataFlavor.javaFileListFlavor);

    						File[] filesTemp = new File[fileList.size()];
    						fileList.toArray(filesTemp);
    						File[] files = filesTemp;

    						if(listener != null)
    							listener.filesDropped(files);

    						evt.getDropTargetContext().dropComplete(true);
    						log( out, "FileDrop: drop complete." );
    					}  
    					else {
    						DataFlavor[] flavors = tr.getTransferDataFlavors();
    						boolean handled = false;
    						for (int zz = 0; zz < flavors.length; zz++) {
    							if (flavors[zz].isRepresentationClassReader()) {
    								evt.acceptDrop(DnDConstants.ACTION_COPY);
    								log(out, "FileDrop: reader accepted.");

    								Reader reader = flavors[zz].getReaderForText(tr);
    								BufferedReader br = new BufferedReader(reader);
    								if(listener != null)
    									listener.filesDropped(createFileArray(br, out));

    								evt.getDropTargetContext().dropComplete(true);
    								log(out, "FileDrop: drop complete.");
    								handled = true;
    								break;
    							}
    						}
    						if(!handled){
    							log( out, "FileDrop: not a file list or reader - abort." );
    							evt.rejectDrop();
    						}       
    					} 
    				}
    				catch (IOException io) {   
    					log( out, "FileDrop: IOException - abort:" );
    					io.printStackTrace( out );
    					evt.rejectDrop();
    				}
    				catch (UnsupportedFlavorException ufe) {   
    					log( out, "FileDrop: UnsupportedFlavorException - abort:" );
    					ufe.printStackTrace( out );
    					evt.rejectDrop();
    				}
    				finally {
    					if(c instanceof JComponent) {   
    						JComponent jc = (JComponent) c;
    						jc.setBorder( normalBorder );
    						log( out, "FileDrop: normal border restored." );
    					}
    				}
    			}

    			public void dragExit(DropTargetEvent evt) {   
    				log(out, "FileDrop: dragExit event.");
    				if(c instanceof JComponent ) {   
    					JComponent jc = (JComponent) c;
    					jc.setBorder( normalBorder );
    					log( out, "FileDrop: normal border restored." );
    				}
    			}

    			public void dropActionChanged(DropTargetDragEvent evt) {   
    				log( out, "FileDrop: dropActionChanged event." );
    				if( isDragOk( out, evt ) ) {
    					evt.acceptDrag(DnDConstants.ACTION_COPY);
    					log( out, "FileDrop: event accepted." );
    				}
    				else {   
    					evt.rejectDrag();
    					log( out, "FileDrop: event rejected." );
    				}
    			}
    		};

    		makeDropTarget( out, c, recursive );
    	}
    	else {   
    		log( out, "FileDrop: Drag and drop is not supported with this JVM" );
    	}
    }

    private static boolean supportsDnD() {
    	if( supportsDnD == null ) {   
    		boolean support = false;
    		try {   
    			support = true;
    		} catch( Exception e ) {   
    			support = false;
    		}
    		supportsDnD = Boolean.valueOf(support);
    	}
    	return supportsDnD.booleanValue();
    }

    private static final String ZERO_CHAR_STRING = "" + (char)0;
    private static File[] createFileArray(BufferedReader bReader, PrintStream out)	{
    	try { 
    		List<File> list = new ArrayList<>();
    		String line = null;
    		while ((line = bReader.readLine()) != null) {
    			try {
    				if(ZERO_CHAR_STRING.equals(line)) continue; 

    				File file = new File(new java.net.URI(line));
    				list.add(file);
    			} catch (Exception ex) {
    				log(out, "Error with " + line + ": " + ex.getMessage());
    			}
    		}

    		return list.toArray(new File[list.size()]);
    	} catch (IOException ex) {
    		log(out, "FileDrop: IOException");
    	}
    	return new File[0];
    }

    private void makeDropTarget(final PrintStream out, final Component c, boolean recursive ) {
    	final DropTarget dt = new DropTarget();
    	try {
    		dt.addDropTargetListener(dropListener);
    	}
    	catch(TooManyListenersException e) {
    		e.printStackTrace();
    		log(out, "FileDrop: Drop will not work due to previous error. Do you have another listener attached?" );
    	}

    	c.addHierarchyListener(new HierarchyListener() {
    		public void hierarchyChanged(HierarchyEvent evt ) {   
    			log( out, "FileDrop: Hierarchy changed." );
    			Component parent = c.getParent();
    			if(parent == null) {
    				c.setDropTarget(null);
    				log( out, "FileDrop: Drop target cleared from component." );
    			}
    			else {
    				new DropTarget(c, dropListener);
    				log( out, "FileDrop: Drop target added to component." );
    			}
    		}
    	});
    	if(c.getParent() != null )
    		new DropTarget(c, dropListener);

    	if(recursive && (c instanceof Container )) {   
    		Container cont = (Container) c;
    		Component[] comps = cont.getComponents();

    		for(int i = 0; i < comps.length; i++ )
    			makeDropTarget( out, comps[i], recursive );
    	}
    }

    private boolean isDragOk(final PrintStream out, final DropTargetDragEvent evt) {   
    	boolean ok = false;
    	DataFlavor[] flavors = evt.getCurrentDataFlavors();
    	int i = 0;
    	while(!ok && i < flavors.length) {   
    		DataFlavor curFlavor = flavors[i];
    		if(curFlavor.equals(DataFlavor.javaFileListFlavor) ||
    				curFlavor.isRepresentationClassReader()){
    			ok = true;
    		}
    		i++;
    	}

    	if(out != null) {   
    		if( flavors.length == 0)
    			log( out, "FileDrop: no data flavors.");
    		for(i = 0; i < flavors.length; i++)
    			log( out, flavors[i].toString());
    	}

    	return ok;
    }
    
    private static void log(PrintStream out, String message) {
    	if(out != null)
    		out.println( message );
    }

    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     * This will recursively unregister all components contained within
     * <var>c</var> if <var>c</var> is a {@link Container}.
     *
     * @param c The component to unregister as a drop target
     * @since 1.0
     */
    public static boolean remove(Component c) {
    	return remove( null, c, true );
    }
    
    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     *
     * @param out Optional {@link PrintStream} for logging drag and drop messages
     * @param c The component to unregister
     * @param recursive Recursively unregister components within a container
     * @since 1.0
     */
    public static boolean remove(PrintStream out, Component c, boolean recursive) {   
    	if(supportsDnD()) {   
    		log( out, "FileDrop: Removing drag-and-drop hooks." );
    		c.setDropTarget(null);

    		if(recursive && (c instanceof Container)) {   
    			Component[] comps = ((Container)c).getComponents();
    			for(int i = 0; i < comps.length; i++)
    				remove( out, comps[i], recursive );

    			return true;
    		}   
    		else return false;
    	}
    	else return false;
    }
        
    /**
     * Implement this inner interface to listen for when files are dropped. For example
     * your class declaration may begin like this:
     * <code><pre>
     *      public class MyClass implements FileDrop.Listener
     *      ...
     *      public void filesDropped( java.io.File[] files )
     *      {
     *          ...
     *      }   // end filesDropped
     *      ...
     * </pre></code>
     *
     * @since 1.1
     */
    public interface Listener {
        /**
         * This method is called when files have been successfully dropped.
         *
         * @param files An array of <tt>File</tt>s that were dropped.
         * @since 1.0
         */
		void filesDropped(File[] files);
    }
        
    /**
     * This is the event that is passed to the
     * {@link FileDropListener#filesDropped filesDropped(...)} method in
     * your {@link FileDropListener} when files are dropped onto
     * a registered drop target.
     *
     * <p>I'm releasing this code into the Public Domain. Enjoy.</p>
     * 
     * @author  Robert Harder
     * @author  rob@iharder.net
     * @version 1.2
     */
    public static class Event extends EventObject {
    	private static final long serialVersionUID = 574338348638475507L;
    	private final File[] files;

    	/**
    	 * Constructs an {@link Event} with the array
    	 * of files that were dropped and the
    	 * {@link FileDrop} that initiated the event.
    	 *
    	 * @param files The array of files that were dropped
    	 * @source The event source
    	 * @since 1.1
    	 */
    	public Event( File[] files, Object source ) {
    		super( source );
    		this.files = files;
    	}

    	/**
    	 * Returns an array of files that were dropped on a
    	 * registered drop target.
    	 *
    	 * @return array of files that were dropped
    	 * @since 1.1
    	 */
    	public File[] getFiles() {
    		return files;
    	}
    }
    
    /**
     * At last an easy way to encapsulate your custom objects for dragging and dropping
     * in your Java programs!
     * When you need to create a {@link Transferable} object,
     * use this class to wrap your object.
     * For example:
     * <pre><code>
     *      ...
     *      MyCoolClass myObj = new MyCoolClass();
     *      Transferable xfer = new TransferableObject( myObj );
     *      ...
     * </code></pre>
     * Or if you need to know when the data was actually dropped, like when you're
     * moving data out of a list, say, you can use the {@link Fetcher}
     * inner class to return your object Just in Time.
     * For example:
     * <pre><code>
     *      ...
     *      final MyCoolClass myObj = new MyCoolClass();
     *
     *      TransferableObject.Fetcher fetcher = new TransferableObject.Fetcher()
     *      {   public Object getObject(){ return myObj; }
     *      }; // end fetcher
     *
     *      Transferable xfer = new TransferableObject( fetcher );
     *      ...
     * </code></pre>
     *
     * The {@link DataFlavor} associated with
     * {@link TransferableObject} has the representation class
     * <tt>net.iharder.dnd.TransferableObject.class</tt> and MIME type
     * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
     * This data flavor is accessible via the static
     * {@link #DATA_FLAVOR} property.
     *
     *
     * <p>I'm releasing this code into the Public Domain. Enjoy.</p>
     * 
     * @author  Robert Harder
     * @author  rob@iharder.net
     * @version 1.2
     */
    public static class TransferableObject implements Transferable {
        /**
         * The MIME type for {@link #DATA_FLAVOR} is 
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";


        /**
         * The default {@link DataFlavor} for
         * {@link TransferableObject} has the representation class
         * <tt>net.iharder.dnd.TransferableObject.class</tt>
         * and the MIME type 
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static DataFlavor DATA_FLAVOR = 
            new DataFlavor( TransferableObject.class, MIME_TYPE );

        private Fetcher fetcher;
        private Object data;
        private DataFlavor customFlavor; 

        /**
         * Creates a new {@link TransferableObject} that wraps <var>data</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class 
         * determined from <code>data.getClass()</code> and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @param data The data to transfer
         * @since 1.1
         */
        public TransferableObject(Object data) {   
        	this.data = data;
            this.customFlavor = new DataFlavor(data.getClass(), MIME_TYPE);
        }

        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * No custom data flavor is set other than the default
         * {@link #DATA_FLAVOR}.
         *
         * @see Fetcher
         * @param fetcher The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject(Fetcher fetcher) {   
        	this.fetcher = fetcher;
        }

        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class <var>dataClass</var>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @see Fetcher
         * @param dataClass The {@link Class} to use in the custom data flavor
         * @param fetcher The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject(Class<?> dataClass, Fetcher fetcher) {   
        	this.fetcher = fetcher;
            this.customFlavor = new DataFlavor( dataClass, MIME_TYPE );
        }

        /**
         * Returns the custom {@link DataFlavor} associated
         * with the encapsulated object or <tt>null</tt> if the {@link Fetcher}
         * constructor was used without passing a {@link Class}.
         *
         * @return The custom data flavor for the encapsulated object
         * @since 1.1
         */
        public DataFlavor getCustomDataFlavor() {   
        	return customFlavor;
        }

        /**
         * Returns a two- or three-element array containing first
         * the custom data flavor, if one was created in the constructors,
         * second the default {@link #DATA_FLAVOR} associated with
         * {@link TransferableObject}, and third the
         * {@link DataFlavor.stringFlavor}.
         *
         * @return An array of supported data flavors
         * @since 1.1
         */
        public DataFlavor[] getTransferDataFlavors() {   
        	if(customFlavor != null)
        		return new DataFlavor[] {   
        				customFlavor,
        				DATA_FLAVOR,
        				DataFlavor.stringFlavor
        	};
        	else
        		return new DataFlavor[] {
        				DATA_FLAVOR,
        				DataFlavor.stringFlavor
        	};
        }

        /**
         * Returns the data encapsulated in this {@link TransferableObject}.
         * If the {@link Fetcher} constructor was used, then this is when
         * the {@link Fetcher#getObject getObject()} method will be called.
         * If the requested data flavor is not supported, then the
         * {@link Fetcher#getObject getObject()} method will not be called.
         *
         * @param flavor The data flavor for the data to return
         * @return The dropped data
         * @since 1.1
         */
        public Object getTransferData(DataFlavor flavor)
        		throws UnsupportedFlavorException, IOException {   

        	if(flavor.equals(DATA_FLAVOR))
        		return fetcher == null ? data : fetcher.getObject();

        	if(flavor.equals(DataFlavor.stringFlavor))
        		return fetcher == null ? data.toString() : fetcher.getObject().toString();

        		throw new UnsupportedFlavorException(flavor);
        }

        /**
         * Returns <tt>true</tt> if <var>flavor</var> is one of the supported
         * flavors. Flavors are supported using the <code>equals(...)</code> method.
         *
         * @param flavor The data flavor to isValid
         * @return Whether or not the flavor is supported
         * @since 1.1
         */
        public boolean isDataFlavorSupported(DataFlavor flavor) {
        	if(flavor.equals(DATA_FLAVOR))
        		return true;

			return flavor.equals(DataFlavor.stringFlavor);
		}
    
        /**
         * Instead of passing your data directly to the {@link TransferableObject}
         * constructor, you may want to know exactly when your data was received
         * in case you need to remove it from its source (or do anyting else to it).
         * When the {@link #getTransferData getTransferData(...)} method is called
         * on the {@link TransferableObject}, the {@link Fetcher}'s
         * {@link #getObject getObject()} method will be called.
         *
         * @author Robert Harder
         * @copyright 2001
         * @version 1.1
         * @since 1.1
         */
        public interface Fetcher {
            
        	/**
             * Return the object being encapsulated in the
             * {@link TransferableObject}.
             *
             * @return The dropped object
             * @since 1.1
             */
			Object getObject();
        }
    }

       
}
