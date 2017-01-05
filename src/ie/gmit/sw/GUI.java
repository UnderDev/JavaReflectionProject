package ie.gmit.sw;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		 new GUI();
	}
	
	
	public GUI() {

	    // Set the frame title
	    super("Drag and drop test");

	    // Set the size
	    this.setSize(250, 150);

	    // Create the label
	    JLabel myLabel = new JLabel("Drag something here!", SwingConstants.CENTER);

	    // Create the drag and drop listener
	    MyDragDropListener myDragDropListener = new MyDragDropListener();

	    // Connect the label with a drag and drop listener
	    new DropTarget(myLabel, myDragDropListener);

	    // Add the label to the content
	    this.getContentPane().add(BorderLayout.CENTER, myLabel);

	    // Show the frame
	    this.setVisible(true);

	}
	
	class MyDragDropListener implements DropTargetListener {

	    @Override
	    public void drop(DropTargetDropEvent event) {

	        // Accept copy drops
	        event.acceptDrop(DnDConstants.ACTION_COPY);

	        // Get the transfer which can provide the dropped item data
	        Transferable transferable = event.getTransferable();

	        // Get the data formats of the dropped item
	        DataFlavor[] flavors = transferable.getTransferDataFlavors();

	        // Loop through the flavors
	        for (DataFlavor flavor : flavors) {

	            try {

	                // If the drop items are files
	                if (flavor.isFlavorJavaFileListType()) {

	                    // Get all of the dropped files
	                    List files = (List) transferable.getTransferData(flavor);

	                    // Loop them through
	                   /* for (File file : files) {
	                        // Print out the file path
	                        System.out.println("File path is '" + file.getPath() + "'.");

	                    }*/

	                }

	            } catch (Exception e) {

	                // Print out the error stack
	                e.printStackTrace();

	            }
	        }

	        // Inform that the drop is complete
	        event.dropComplete(true);

	    }

	    @Override
	    public void dragEnter(DropTargetDragEvent event) {
	    }

	    @Override
	    public void dragExit(DropTargetEvent event) {
	    }

	    @Override
	    public void dragOver(DropTargetDragEvent event) {
	    }

	    @Override
	    public void dropActionChanged(DropTargetDragEvent event) {
	    }

	}

}
