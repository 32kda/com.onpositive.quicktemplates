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
		if (parameter != null) {
			int option = Integer.parseInt(parameter.trim());
			switch (option) {
				case 1: insertTemplate("org.eclipse.jdt.ui.templates.if", sourceViewer, unit);
				break;
				case 2: insertTemplate("org.eclipse.jdt.ui.templates.ifnotnull",sourceViewer, unit);
				break;
				case 3: insertTemplate("org.eclipse.jdt.ui.templates.ifnull",sourceViewer, unit);
				break;
				case 4: insertTemplate("com.onpositive.quicktemplates.arrlist",sourceViewer, unit);
				break;
				case 5: insertTemplate("com.onpositive.quicktemplates.hashmap",sourceViewer, unit);
				break;
			}
			
		}
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
