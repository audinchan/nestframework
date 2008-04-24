/**
 * 
 */
package org.nestframework.ide.editors.hyperlink;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.internal.compiler.apt.model.PackageElementImpl;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.corext.dom.NodeFinder;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.ASTProvider;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.IClassFileEditorInput;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author audin
 *
 */
@SuppressWarnings("unchecked")
public class NestActionHyperlinkDetector extends AbstractHyperlinkDetector {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlinkDetector#detectHyperlinks(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion, boolean)
	 */
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		ITextEditor textEditor= (ITextEditor)getAdapter(ITextEditor.class);
		if (region == null || textEditor == null || canShowMultipleHyperlinks)
			return null;

		IEditorSite site= textEditor.getEditorSite();
		if (site == null)
			return null;

		IJavaElement javaElement= getInputJavaElement(textEditor);
		if (javaElement == null)
			return null;

		CompilationUnit ast= JavaPlugin.getDefault().getASTProvider().getAST(javaElement, ASTProvider.WAIT_NO, null);
		if (ast == null)
			return null;

		ASTNode node= NodeFinder.perform(ast, region.getOffset(), 1);
		if (!(node instanceof StringLiteral)  && !(node instanceof SimpleName))
			return null;
		
		if (node.getLocationInParent() == QualifiedName.QUALIFIER_PROPERTY)
			return null;

		
//		AccessorClassReference ref= NLSHintHelper.getAccessorClassReference(ast, nlsKeyRegion);
//		if (ref == null)
//			return null;
		String fileName= null;
		if (node instanceof StringLiteral) {
			fileName= ((StringLiteral)node).getLiteralValue();
		} else {
			fileName= ((SimpleName)node).getIdentifier();
		}
		if (fileName != null) {
			int extLen = 3;
			int pos = fileName.indexOf(".jsp");
			if (pos != -1) { // 处理forward
				fileName = fileName.substring(0, pos +1 + extLen);
				IRegion fileRegion= new Region(node.getStartPosition()+1, pos+1+extLen);
				return new IHyperlink[] {new NestFileHyperlink(fileRegion, textEditor, fileName, javaElement)};
			} else { // 处理NestAction
				pos = fileName.indexOf(".a");
				extLen = 1;
				if (pos != -1) {
					fileName = fileName.substring(0, pos +1 + extLen);
					IRegion fileRegion= new Region(node.getStartPosition()+1, pos+1+extLen);
					IJavaElement pkg = javaElement.getParent();
					String classname = null;
					if (pkg instanceof PackageFragment) {
						classname = ((PackageFragment) pkg).getElementName();
					} else {
						return null;
					}
					String actionclass = fileName;
					if (actionclass.charAt(0) == '!') {
						actionclass = actionclass.substring(1);
					}
					while (actionclass.startsWith("../")) {
						actionclass = actionclass.substring(3);
						classname = classname.substring(0, classname.lastIndexOf('.'));
					}
					classname = classname + "." + actionclass;
					classname = classname.replaceAll("/", ".").replaceAll("\\.a([?].*)?$", "");
					
					try {
						IType actionType = javaElement.getJavaProject().findType(classname);
						return new IHyperlink[] {new NestActionHyperlink(fileRegion, actionType)};
					} catch (JavaModelException e) {
					}
				}
			}
		}

		return null;
	}
	
	private IJavaElement getInputJavaElement(ITextEditor editor) {
		IEditorInput editorInput= editor.getEditorInput();
		if (editorInput instanceof IClassFileEditorInput)
			return ((IClassFileEditorInput)editorInput).getClassFile();

		if (editor instanceof CompilationUnitEditor)
			return JavaPlugin.getDefault().getWorkingCopyManager().getWorkingCopy(editorInput);

		return null;
	}
}
