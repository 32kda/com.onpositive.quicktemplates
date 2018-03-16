package com.onpositive.quicktemplates.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.internal.corext.template.java.CompilationUnitContext;
import org.eclipse.jdt.internal.corext.template.java.JavaContext;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Point;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
@SuppressWarnings("restriction")
public class QuickTemplateHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AbstractTextEditor activeEditor = 
		        (AbstractTextEditor) HandlerUtil.getActiveEditor(event);
		if (activeEditor == null) {
			return null;
		}
		ITypeRoot element = EditorUtility.getEditorInputJavaElement(activeEditor, true);
		if (element == null) {
			return null;
		}
		ICompilationUnit unit = element.getAdapter(ICompilationUnit.class);
		if (unit == null) {
			return null;
		}
		ISourceViewer sourceViewer = (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);
		String parameter = event.getParameter("com.onpositive.quicktemplates.commands.slotId");
//		Point range = sourceViewer.getSelectedRange();
//		JavaPlugin.getDefault().getTemplateStore().getTemplateData()
//		JavaPlugin.getDefault().getTemplateContextRegistry().getContextType("java-statements").
		// You can generate template dynamically here!
//		Template template = new Template("new List", "Add new list creation", JavaContextType.ID_STATEMENTS,
//				"List<${type}> ${name:newName(java.util.List)} = new ArrayList<${type}>();${:import(java.util.List, java.util.ArrayList)}",
//				true);
//		IRegion region = new Region(range.x, range.y);
//		JavaContextType contextType = new JavaContextType();
//		contextType.setId(JavaContextType.ID_STATEMENTS); //Set context type, for which we apply this template
//		contextType.addResolver(new ImportsResolver("import","import")); //Add imports resolver if we want imports to be added automatically for some template
//		CompilationUnitContext ctx = new JavaContext(contextType, sourceViewer.getDocument(), range.x,
//				range.y, unit);
//		TemplateProposal proposal = new TemplateProposal(template, ctx, region, null);
//		proposal.apply(sourceViewer, (char) 0, 0, 0);
		insertTemplate("com.onpositive.quicktemplates.ifnotnull",sourceViewer, unit);
		return null;
	}
	
	protected void insertTemplate(String templateId, ITextViewer sourceViewer, ICompilationUnit unit) {
		Point range = sourceViewer.getSelectedRange();
		IRegion region = new Region(range.x, range.y);
		Template template = JavaPlugin.getDefault().getTemplateStore().getTemplateData(templateId).getTemplate();
		TemplateContextType contextType = JavaPlugin.getDefault().getTemplateContextRegistry().getContextType(template.getContextTypeId());
		CompilationUnitContext ctx = new JavaContext(contextType, sourceViewer.getDocument(), range.x,
				range.y, unit);
		TemplateProposal proposal = new TemplateProposal(template, ctx, region, null);
		proposal.apply(sourceViewer, (char) 0, 0, 0);
	}
}
