package org.nestframework.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Represents a file that was submitted as part of an HTTP POST request.  Provides methods for
 * examining information about the file, and the retreiving the contents of the file. When a file
 * is uploaded by a user it is stored as a temporary file on the file system, which is wrapped by an
 * instance of this class. This is necessary because browsers may send file upload segments before
 * sending any other form parameters needed to identify what to do with the uploaded files!</p>
 *
 * <p>The application developer is responsible for removing this temporary file once they have
 * processed it.  This can be accomplished in one of two ways.  Firstly a call to save(File) will
 * effect a save by <em>moving</em> the temporary file to the desired location.  In this case there
 * is no need to call delete(), although doing so will not delete the saved file. The second way is
 * to simply call delete().  This is more applicable when consuming the file as an InputStream. An
 * exmaple code fragment for reading a text based file might look like this:</p>
 *
 * <pre>
 * FileBean bean = getUserIcon();
 * BufferedReader reader = new BufferedReader( new InputStreamReader(bean.getInputStream()) );
 * String line = null
 *
 * while ( (line = reader.readLine()) != null) {
 *     // do something with line
 * }
 *
 * bean.delete();
 * </pre>
 *
 * @author Tim Fennell
 */
public class FileItem {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(FileItem.class);
	
	public static final int BUFFER_SIZE = 4096;

    private String contentType;
    private String fileName;
    private File file;
    private boolean saved;


    /**
     * Constructs a FileBean pointing to an on-disk representation of the file uploaded by the user.
     *
     * @param file the File object on the server which holds the uploaded contents of the file
     * @param contentType the content type of the file declared by the browser during uplaod
     * @param originalName the name of the file as declared by the user&apos;s browser
     */
    public FileItem(File file, String contentType, String originalName) {
        this.file = file;
        this.contentType = contentType;
        this.fileName = originalName;
    }

    /**
     * Returns the name of the file that the user selected and uplaoded (this is not necessarily
     * the name that the underlying file is now stored on the server using).
     */
    public String getFileName() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileName() - start");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getFileName() - end");
		}
        return fileName;
    }

    /**
     * Returns the content type of the file that the user selected and uplaoded.
     */
    public String getContentType() {
		if (logger.isDebugEnabled()) {
			logger.debug("getContentType() - start");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getContentType() - end");
		}
        return contentType;
    }

    /**
     * Gets the size of the file that was uploaded.
     */
    public long getSize() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSize() - start");
		}

		long returnlong = this.file.length();
		if (logger.isDebugEnabled()) {
			logger.debug("getSize() - end");
		}
        return returnlong;
    }

    /**
     * Gets an input stream to read from the file uploaded
     */
    public InputStream getInputStream() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("getInputStream() - start");
		}

		InputStream returnInputStream = new FileInputStream(this.file);
		if (logger.isDebugEnabled()) {
			logger.debug("getInputStream() - end");
		}
        return returnInputStream;
    }

    /**
     * Saves the uploaded file to the location on disk represented by File.  First attemps a
     * simple rename of the underlying file that was created during upload as this is the
     * most efficient route. If the rename fails an attempt is made to copy the file bit
     * by bit to the new File and then the temporary file is removed.
     *
     * @param toFile a File object representing a location
     * @throws IOException if the save will fail for a reason that we can detect up front, for
     *         example, missing files, permissions etc. or we try to save get a failure.
     */
    public void save(File toFile) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("save(File) - start");
		}

        // Since File.renameTo doesn't tell you anything about why it failed, we test
        // for some common reasons for failure ahead of time and give a bit more info
        if (!this.file.exists()) {
            throw new IOException
                ("Some time between uploading and saving we lost the file "
                    + this.file.getAbsolutePath() + " - where did it go?.");
        }

        if (!this.file.canWrite()) {
            throw new IOException
                ("Some time between uploading and saving we lost the ability to write to the file "
                    + this.file.getAbsolutePath() + " - writability is required to move the file.");
        }

        File parent = toFile.getAbsoluteFile().getParentFile();
        if (toFile.exists() && !toFile.canWrite()) {
            throw new IOException("Cannot overwrite existing file at "+ toFile.getAbsolutePath());
        }
        else if (!parent.exists() && !parent.mkdirs()) {
            throw new IOException("Parent directory of specified file does not exist and cannot " +
                " be created. File location supplied: " + toFile.getAbsolutePath());
        }
        else if (!toFile.exists() && !parent.canWrite()) {
            throw new IOException("Cannot create new file at location: " + toFile.getAbsolutePath());
        }

        this.saved = this.file.renameTo(toFile);

        // If the rename didn't work, try copying the darn thing bit by bit
        if (this.saved == false) {
            saveViaCopy(toFile);
        }

		if (logger.isDebugEnabled()) {
			logger.debug("save(File) - end");
		}
    }

    /**
     * Attempts to save the uploaded file to the specified file by performing a stream
     * based copy. This is only used when a rename cannot be executed, e.g. because the
     * target file is on a different file system than the temporary file.
     *
     * @param toFile the file to save to
     */
    protected void saveViaCopy(File toFile) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("saveViaCopy(File) - start");
		}

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(toFile));
        BufferedInputStream   in = new BufferedInputStream(new FileInputStream(this.file));

        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }

        in.close();
        out.close();

        this.file.delete();
        this.saved = true;

		if (logger.isDebugEnabled()) {
			logger.debug("saveViaCopy(File) - end");
		}
    }
    
    public byte[] getBytes() throws IOException {
		if (this.file != null && !this.file.exists()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
		return copyToByteArray(this.file);
	}
    
	public static byte[] copyToByteArray(File in) throws IOException {
		return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
	}
	
    public static byte[] copyToByteArray(InputStream in) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("copyToByteArray(InputStream) - start");
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		byte[] returnbyteArray = out.toByteArray();
		if (logger.isDebugEnabled()) {
			logger.debug("copyToByteArray(InputStream) - end");
		}
		return returnbyteArray;
	}
    
	public static int copy(InputStream in, OutputStream out) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("copy(InputStream, OutputStream) - start");
		}

		if (in == null) {
			throw new IOException("No InputStream specified");
		}
		if (out == null) {
			throw new IOException("No OutputStream specified");
		}
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();

			if (logger.isDebugEnabled()) {
				logger.debug("copy(InputStream, OutputStream) - end");
			}
			return byteCount;
		}
		finally {
			try {
				in.close();
			}
			catch (IOException ex) {
				logger.warn("Could not close InputStream", ex);
			}
			try {
				out.close();
			}
			catch (IOException ex) {
				logger.warn("Could not close OutputStream", ex);
			}
		}
	}

    /**
     * Deletes the temporary file associated with this file upload if one still exists.  If save()
     * has already been called then there is no temporary file any more, and this is a no-op.
     *
     * @throws IOException if the delete will fail for a reason we can detect up front, or if
     *         we try to delete and get a failure
     */
    public void delete() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("delete() - start");
		}

        if (!this.saved) {
            // Since File.delete doesn't tell you anything about why it failed, we test
            // for some common reasons for failure ahead of time and give a bit more info
            if (!this.file.exists()) {
                throw new IOException
                    ("Some time between uploading and saving we lost the file "
                        + this.file.getAbsolutePath() + " - where did it go?.");
            }

            if (!this.file.canWrite()) {
                throw new IOException
                    ("Some time between uploading and saving we lost the ability to write to the file "
                        + this.file.getAbsolutePath() + " - writability is required to delete the file.");
            }
            this.file.delete();
        }

		if (logger.isDebugEnabled()) {
			logger.debug("delete() - end");
		}
    }

    /**
     * Returns the name of the file and the content type in a String format.
     */
    public String toString() {
		if (logger.isDebugEnabled()) {
			logger.debug("toString() - start");
		}

		String returnString = "FileBean{" + "contentType='" + contentType + "'"
				+ ", fileName='" + fileName + "'" + "}";
		if (logger.isDebugEnabled()) {
			logger.debug("toString() - end");
		}
        return returnString;
    }
    
    /**
     * Detect whether file is uploaded successfully.
     * @return
     */
    public boolean isUploaded() {
    	return file != null && file.exists() && file.isFile();
    }
}
