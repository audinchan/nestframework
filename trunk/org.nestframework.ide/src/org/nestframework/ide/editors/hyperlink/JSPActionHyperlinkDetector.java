package org.nestframework.ide.editors.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class JSPActionHyperlinkDetector extends AbstractHyperlinkDetector {
	public static final String BASE = "/WebContent/";

	public static final String PKG_PREFIX = "com.becom.";

	public static final String ACTION_PREFIX = ".webapp.action.";

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		if (textViewer != null && region != null) {
			IDocument doc = textViewer.getDocument();
			if (doc != null) {
				try {
					IRegion actionRegion = null;
					String actionClass = null;
					Node currentNode = getCurrentNode(doc, region.getOffset());
					if (currentNode != null
							&& currentNode.getNodeType() == Node.ELEMENT_NODE) {
						if ("form".equalsIgnoreCase(currentNode.getNodeName())) {
							NamedNodeMap attrs = currentNode.getAttributes();
							Node item = attrs.getNamedItem("action");
							actionClass = item.getNodeValue();
							if (item instanceof AttrImpl) {
								AttrImpl attr = (AttrImpl) item;
								actionRegion = new Region(attr.getValueRegionStartOffset()+1, actionClass.length());
							}
						} else if ("a".equals(currentNode.getNodeName())) {
							NamedNodeMap attrs = currentNode.getAttributes();
							Node item = attrs.getNamedItem("href");
							actionClass = item.getNodeValue();
							if (item instanceof AttrImpl) {
								AttrImpl attr = (AttrImpl) item;
								actionRegion = new Region(attr.getValueRegionStartOffset()+1, actionClass.length());
							}
						}
					}
					if (actionClass != null) {
						IStructuredModel sModel = StructuredModelManager
								.getModelManager().getExistingModelForRead(doc);

						IFile file = ResourcesPlugin.getWorkspace().getRoot()
								.getFile(new Path(sModel.getBaseLocation()));
						IJavaProject jProj = (IJavaProject) file.getProject()
								.getNature(JavaCore.NATURE_ID);
						if (actionClass.startsWith("${")) { // 处理${ctx }这样的情况
							actionClass = actionClass.replaceAll("^[$][{].+[}]/", "/");
						} else if (actionClass.startsWith("<%")) { // 处理<%=request.xxxxx%>这样的情况
							actionClass = actionClass.replaceAll("^<.+>/", "/");
						}
						if (!actionClass.startsWith("/")) {

							String classname = sModel.getBaseLocation();
							int p = classname.indexOf(BASE);
							int p2 = classname.lastIndexOf('/');
							classname = classname.substring(p + BASE.length(),
									p2);

							// System.out.println(actionClass);
							// System.out.println(classname);
							while (actionClass.startsWith("../")) {
								actionClass = actionClass.substring(3);
								int p3 = classname.lastIndexOf('/');
								classname = classname.substring(0, p3);
							}
							actionClass = classname + "." + actionClass;
						} else {
							actionClass = actionClass.substring(1);
						}

						actionClass = actionClass.replaceAll("/", ".")
								.replaceAll("\\.a([?].*)?$", "").trim();

						actionClass = PKG_PREFIX + jProj.getProject().getName()
								+ ACTION_PREFIX + actionClass;

//						System.out.println(actionClass);

						if (actionRegion == null) {
							actionRegion = region;
						}
						IType type = jProj.findType(actionClass);
						return new IHyperlink[] { new NestActionHyperlink(
								actionRegion, type) };
					}

				} catch (CoreException e) {
				}
			}
		}
		return null;
	}

	/**
	 * Returns the node the cursor is currently on in the document. null if no
	 * node is selected
	 * 
	 * @param offset
	 * @return Node either element, doctype, text, or null
	 */
	private Node getCurrentNode(IDocument document, int offset) {
		// get the current node at the offset (returns either: element,
		// doctype, text)
		IndexedRegion inode = null;
		IStructuredModel sModel = null;
		try {
			sModel = StructuredModelManager.getModelManager()
					.getExistingModelForRead(document);
			inode = sModel.getIndexedRegion(offset);
			if (inode == null)
				inode = sModel.getIndexedRegion(offset - 1);
		} finally {
			if (sModel != null)
				sModel.releaseFromRead();
		}

		if (inode instanceof Node) {
			return (Node) inode;
		}
		return null;
	}
}
