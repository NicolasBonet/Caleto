package caleto.logic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class Caleto
{
	/**
     *  Image file used to hide a file
     */
	private File imageFile;
	
	/**
     *  File to be hidden in an image
     */
	private File fileToBeHidden;
	
	/**
     *  Image file where the file is hidden
     */
	private File recoveryImageFile;
	
	/**
     *  Message used when everything went just fine hidding a file
     */
	private final static String HIDDEN_FILE = "File has been succesfully hidden!";
	
	/**
     *  Message used when file to be hidden and/or image weren't selected
     */
	private final static String NO_FILES = "You must select both image file and file to be hidden first!";
	
	/**
     *  Message used when the file is too big to be hidden in that image
     */
	private final static String NO_SPACE = "File to be hidden is too big to be protected under that image";
	
	/**
     *  Message used when everything went just fine
     */
	private final static String NOT_POSSIBLE = "It wasn't possible to create the file!";
	
	/**
     *  Message used when file to be recovered and/or destination file weren't selected
     */
	private final static String RECOVER_NO_FILES = "You must select both image file and destination file first!";
	
	/**
     *  Message used when everything went just fine recovering a file 
     */
	private final static String RECOVER_RECOVERED_FILE = "File has been succesfully recovered!";
	
	/**
     *  Caleto constructor!
     */
	public Caleto()
	{
		imageFile = fileToBeHidden = recoveryImageFile = null;
	}

	/**
     *  It sets the image file that we will use to hide the file
     */
	public void setImageFile(File choosedFile) {
		imageFile = choosedFile;
	}
	
	/**
     *  It sets the file to be hidden
     */
	public void setFileToBeHidden(File choosedFile){
		fileToBeHidden = choosedFile;
	}
	
	/**
     *  It sets the recovery image file that we will use to hide the file
     */
	public void setRecoveryImageFile(File choosedFile) {
		recoveryImageFile = choosedFile;
	}
	
	/**
     *  Hides a file!
     */
	public String hideFile(File mergedFile)
	{	
		// No files, no party
		if (fileToBeHidden == null || imageFile == null)
			return NO_FILES;
		
		// Continue...
		try
		{
			// Load information from our file to be hidden
			RandomAccessFile f = new RandomAccessFile(fileToBeHidden.getPath(), "r");
			byte[] bytesToHide = new byte[(int) f.length()];
			f.read(bytesToHide);
			f.close();
			
			// Load information from our image file
			f = new RandomAccessFile(imageFile.getPath(), "r");
			byte[] imageBytes = new byte[(int) f.length()];
			f.read(imageBytes);
			
			// Now prepare the new imagesBytes
			byte[] newImageBytes = new byte[(int) f.length()];
			f.read(newImageBytes);
			f.close();
			
			// Can we save it?
			if (imageBytes.length < (bytesToHide.length * 8))
				return NO_SPACE;
			
			// Now modify the bytes
			int posBit = 0;
			
			// Process our bytes!
			for (int i = 0; i < imageBytes.length; i++)
			{
				// Only non headers bits are changed
				if (i > 54 && (posBit / 8) < bytesToHide.length)
				{
					// Our byte!
					byte imageByte = imageBytes[i];
				   
					// Store what we need
					if (getBit(bytesToHide, posBit) == 1) 
					{
						imageByte |= (1 << 0);
					}
					else
					{
						imageByte &= ~(1 << 0);
					}
					
					// Now we need the next bit!
					posBit++;
					newImageBytes[i] = imageByte;
				}
				// The header remains the same
				else
				{
					newImageBytes[i] = imageBytes[i];
				}
			}
			
			// Store the size of our file!
			int_to_bytes(newImageBytes, 6, bytesToHide.length);
			
			// Write the new file!
			mergedFile.createNewFile();
			OutputStream out = null;
			
			// Not extension?
			String ext = "";
			
			// Add bmp if it's not there!
			if (!mergedFile.getName().endsWith(".bmp"))
				ext += ".bmp";

			// Finish it!
			try {
			    out = new BufferedOutputStream(new FileOutputStream(mergedFile.getPath() + ext));
			    out.write(newImageBytes);
			} finally {
			    if (out != null) out.close();
			}
			
			// Notify!
			return HIDDEN_FILE;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return NOT_POSSIBLE;
		}
	}

	/**
     *  It recovers a hidden file
     */
	public String recoverHiddenFile(File fileToBeRecovered)
	{
		// No files, no party
		if (recoveryImageFile == null)
			return RECOVER_NO_FILES;
				
		// Do the process...
		try
		{
			// Load information from our image file
			RandomAccessFile f = new RandomAccessFile(recoveryImageFile.getPath(), "r");
			byte[] imageBytes = new byte[(int) f.length()];
			f.read(imageBytes);
			f.close();
			
			// Get the number of bytes from our old file!
			int numberOfBytes = bytes_to_int(imageBytes, 6);
			
			// Now gather the bytes we need!
			byte[] recoveredBytes = new byte[numberOfBytes];
			
			// Browse them!
			int bitPos = 0;
			int bytePos = 0;
			
			// We need to start after the header!
			for (int i = 55; i < imageBytes.length && bytePos < numberOfBytes; i++)
			{
				// Set our bit!
				int newBit = getBit(imageBytes, (i + 1) * 8 - 1);
				setBit(recoveredBytes, bitPos, newBit);
				
				// Move one bit forward!
				bitPos++;
				
				// Restart if we've finished our bit and move to the next byte
				if ((bitPos % 8) == 0)
				{
					bytePos++;
				}
			}
			
			// Write the bytes!
			FileOutputStream fos = new FileOutputStream(fileToBeRecovered.getPath());
			fos.write(recoveredBytes);
			fos.close();
			
			// Notify
			return RECOVER_RECOVERED_FILE;
		}
		catch (Exception e) {
			e.printStackTrace();
			return NOT_POSSIBLE;
		}
	}
	
	/**
     *  Helper to get the value of a bit in an specific position
     *  Retrieved from: http://www.herongyang.com/
     */
	private static int getBit(byte[] data, int pos)
	{
		int posByte = pos / 8; 
		int posBit = pos % 8;
		byte valByte = data[posByte];
		int valInt = valByte>>(8-(posBit+1)) & 0x0001;
		return valInt;
	}
	
	/**
     *  Helper to set the value of a bit in an specific position
     *  Retrieved from: http://www.herongyang.com/
     */
	private static void setBit(byte[] data, int pos, int val)
	{
	      int posByte = pos / 8; 
	      int posBit = pos % 8;
	      byte oldByte = data[posByte];
	      oldByte = (byte) (((0xFF7F>>posBit) & oldByte) & 0x00FF);
	      byte newByte = (byte) ((val<<(8-(posBit+1))) | oldByte);
	      data[posByte] = newByte;
	}
	
	/**
     *  Write an integer to a byte array at a specific offset. 
     */
    private static void int_to_bytes(byte[] bytes, int startoffset, int value) {
        bytes[startoffset] = (byte) (value);
        bytes[startoffset+1] = (byte) (value >>> 8);
        bytes[startoffset+2] = (byte) (value >>> 16);
        bytes[startoffset+3] = (byte) (value >>> 24);
    }
    
    /**
     * Read an integer value from a 4-byte array.
     */
    private static int bytes_to_int(byte[] value, int startoffset) {
        return ((value[startoffset+3] & 0xff) << 24) |
            ((value[startoffset+2] & 0xff) << 16) |
            ((value[startoffset+1] & 0xff) << 8) |
            (value[startoffset] & 0xff);
    }
}
