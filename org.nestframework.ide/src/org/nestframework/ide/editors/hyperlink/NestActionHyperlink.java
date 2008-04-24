package org.nestframework.ide.editors.hyperlink;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.PartInitException;

public class NestActionHyperlink implements IHyperlink {
	private final IRegion region;
	private final IJavaElement element;

	public NestActionHyperlink(IRegion region, IJavaElement element) {
		this.region = region;
		this.element = element;
	}

	public IRegion getHyperlinkRegion() {
		// TODO Auto-generated method stub
		return region;
	}

	public String getHyperlinkText() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void open() {
		// TODO Auto-generated method stub
		if (element != null) {
			try {
				JavaUI.revealInEditor(JavaUI.openInEditor(element), element);
			} catch (PartInitException e) {
			} catch (JavaModelException e) {
			}
		}
	}

}
