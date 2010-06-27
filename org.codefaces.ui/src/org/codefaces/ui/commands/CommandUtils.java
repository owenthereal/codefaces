package org.codefaces.ui.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codefaces.ui.CodeFacesUIActivator;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.CommandException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

public class CommandUtils {
	public static void executeCommand(String commandId,
			Map<String, String> parameterMap, Map<String, Object> variableMap) {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		ICommandService cmdService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		Command command = cmdService.getCommand(commandId);

		try {
			List<Parameterization> parameterizations = new ArrayList<Parameterization>();

			// adding parameters
			if (parameterMap != null) {
				for (Entry<String, String> parameterEntry : parameterMap
						.entrySet()) {
					IParameter parameter = command.getParameter(parameterEntry
							.getKey());
					Parameterization parameterization = new Parameterization(
							parameter, parameterEntry.getValue());
					parameterizations.add(parameterization);
				}
			}

			ParameterizedCommand paraCommand = new ParameterizedCommand(
					command, parameterizations.toArray(new Parameterization[0]));
			ExecutionEvent exeEvent = handlerService.createExecutionEvent(
					paraCommand, null);

			// adding variables
			if (variableMap != null) {
				IEvaluationContext context = (IEvaluationContext) exeEvent
						.getApplicationContext();
				for (Entry<String, Object> variableEntry : variableMap
						.entrySet()) {
					context.addVariable(variableEntry.getKey(),
							variableEntry.getValue());
				}
			}

			command.executeWithChecks(exeEvent);
		} catch (CommandException e) {
			IStatus status = new Status(Status.ERROR,
					CodeFacesUIActivator.PLUGIN_ID,
					"Errors occurs when executing command " + commandId, e);
			CodeFacesUIActivator.getDefault().getLog().log(status);
		}

	}
}
