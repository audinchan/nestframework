/**
 * 
 */
package org.nestframework.ide.editors.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * @author audin
 *
 */
public class NestFileHyperlink implements IHyperlink {
	private IRegion region;
	private String fileName;
	private IJavaElement je;
	
	public NestFileHyperlink(IRegion region, IEditorPart editor, String fileName, IJavaElement je) {
		this.region = region;
		this.fileName = fileName;
		this.je = je;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getHyperlinkRegion()
	 */
	public IRegion getHyperlinkRegion() {
		return region;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getHyperlinkText()
	 */
	public String getHyperlinkText() {
		return fileName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getTypeLabel()
	 */
	public String getTypeLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlink#open()
	 */
	public void open() {
		// 第一个版本，使用固定的目录
		String pkgPath = je.getParent().getPath().toString();
		int p = pkgPath.indexOf("/webapp/action");
		if (p != -1) {
			String filePath = null;
			pkgPath = pkgPath.substring(p + "/webapp/action".length());
			if (fileName.charAt(0) == '/') {
				filePath = fileName;
			} else {
				filePath = "/" + pkgPath + "/" + fileName;
			}
			Path path = new Path("/"+je.getJavaProject().getProject().getName() + "/WebContent" + filePath);
			try {
				IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if (f != null && f.exists()) {
					IDE.openEditor(page, f);
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		
	
	}

}
