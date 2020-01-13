package plugintest4;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import plugintest4.listener.fileopening.OpenLocalFileSystemButtonListener;
import plugintest4.listener.fileopening.OpenWorkspaceButtonListener;
import plugintest4.serviceproviders.NameServiceProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class ChoseFileTab extends AbstractLaunchConfigurationTab {

	// TODO output definieren können (niedrigere Prio)
	// TODO choose all, choose non button
	
	private Label helloText;
	private static final String TEXT_LOAD_TXT_FILE = "Load txt File";
	private Text textTxtIn;
	private static final String[] txtFileExtensions = new String[] { "*.txt" };
	private Button loadTxtFileButton;
	private List<String> dummy_text = new ArrayList<>();
	private String dummy_text_text = "This is a dummy text";
	
	private Map<String, Button> analysisButtons;
	private List<IAnalysis> analysis;
	
	private Map<String, Button> serviceButtons;
	private List<NameServiceProvider> services;
	private List<String> executionServiceClassNames;
	
	public ChoseFileTab(List<IAnalysis> analysis) {
		this.analysis = analysis;
		this.analysisButtons = new HashMap<>();
		
		this.serviceButtons = new HashMap<>();
		this.services = new LinkedList<>();
		this.executionServiceClassNames = new LinkedList<String>();
	}
	
	@Override
	public void createControl(Composite parent) {
		// ---------- Setup Tab
		final Composite container = new Composite(parent, SWT.NONE);
		setControl(container);

		GridLayoutFactory.swtDefaults().applyTo(container);

		this.helloText = new Label(container, SWT.LEFT);
		this.helloText.setText("This Run Config lets you analyze text files. "
				+ "Please chose a file here. Analysis can be chosen in the next Tab.");

		// ---------- Create Listeners
		final ModifyListener modifyListener = (ModifyEvent e) -> {
			setDirty(true);
			updateLaunchConfigurationDialog();
		};

		final SelectionListener checkboxSelectionListener = new SelectionAdapter () {
	         public void widgetSelected(SelectionEvent event) {
	             setDirty(true);
	 			 updateLaunchConfigurationDialog();
	          };
	       };
		
		Shell shell = getShell();

		// ---------- Load Files
		this.textTxtIn = new Text(container, SWT.SINGLE | SWT.BORDER);
		this.textTxtIn.setToolTipText("Input txt file(s) [*.txt]");
		this.textTxtIn.setText(dummy_text_text);
		GridData gd1 = new GridData(800, 30);
		this.textTxtIn.setLayoutData(gd1);
        this.textTxtIn.addModifyListener(modifyListener);
		
		SelectionListener localFileSystemListener = new OpenLocalFileSystemButtonListener(textTxtIn,
				txtFileExtensions, TEXT_LOAD_TXT_FILE, shell, true);

        final Button localFileSystemButton = new Button(container, SWT.NONE);
        localFileSystemButton.setText("Chose txt file...");
        localFileSystemButton.addSelectionListener(localFileSystemListener);
        
        // ----------- Load Analysis
        for (IAnalysis ana : analysis) {
        	// create Button to chose Analysis
        	Button b1 = new Button(container, SWT.CHECK);
        	b1.setText(ana.getName());
        	b1.addSelectionListener(checkboxSelectionListener);
        	this.analysisButtons.put(ana.getName(), b1);
        }   
        
        // ----------- Load Analysis from Service Providers
        ClassLoader classloader = AnalyzerAttributes.getURLCL();
        // Ich denke das problem ist entweder im Class loader oder im analyse Plugin
        ServiceLoader<NameServiceProvider> nameServices = 
        		ServiceLoader.load(NameServiceProvider.class, classloader);
        for (NameServiceProvider service : nameServices) {
        	System.out.println("in service loop");
			Button b1 = new Button(container, SWT.CHECK);
        	b1.setText(service.getName());
        	b1.addSelectionListener(checkboxSelectionListener);
        	this.serviceButtons.put(service.getName(), b1);
        	this.services.add(service);
        	this.executionServiceClassNames.add(service.getExecutionServiceProviderName());
		}
	}


	@Override
	public String getName() {
		return "Data";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) { 
		
		try {
			
			// ---- Files
			List<String> fileNames = configuration.getAttribute(AnalyzerAttributes.FILE_NAME, dummy_text);
			String fileName = String.join(";", fileNames);
			this.textTxtIn.setText(fileName);
			
			// ---- Analysis
			Map<String, String> checkbox_activation = configuration
					.getAttribute(AnalyzerAttributes.CHECKBOX_ACTIVATION, new HashMap<String, String>());
			for (Map.Entry<String,String> entry : checkbox_activation.entrySet()) {
				analysisButtons.get(entry.getKey()).setSelection(Boolean.valueOf(entry.getValue()));
			}
			
			// ---- Services
			Map<String, String> service_checkbox_values = configuration
					.getAttribute(AnalyzerAttributes.SERVICE_CHECKBOX_VALUES, new HashMap<String, String>());
			for (Map.Entry<String,String> service_entry : service_checkbox_values.entrySet()) {
				serviceButtons.get(service_entry.getKey()).setSelection(Boolean.valueOf(service_entry.getValue()));
				// TODO abfangen wenn jetzt nicht die gleichen Analysen geladen werden als beim letzten mal
			}
			this.executionServiceClassNames = 
					configuration.getAttribute(AnalyzerAttributes.EXECUTION_SERVICE_CLASS_NAMES, new LinkedList<String>());
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) { 
		System.out.println("perform Apply");
		
		// ---- Files
		List<String> file_names = Arrays.asList(this.textTxtIn.getText().split(";"));
		configuration.setAttribute(AnalyzerAttributes.FILE_NAME, file_names);
		
		// ---- Analysis
		// We have to convert bools to string because attributes can either save only bool or a map of string,string.
		Map<String, String> checkbox_activation = new HashMap<>(); 
		
		for (Map.Entry<String, Button> entry : analysisButtons.entrySet()) {
			checkbox_activation.put(entry.getKey(), Boolean.valueOf(entry.getValue().getSelection()).toString());
		}
		configuration.setAttribute(AnalyzerAttributes.CHECKBOX_ACTIVATION, checkbox_activation);
		
		// ---- Services
		Map<String, String> service_activation = new HashMap<>(); 
		
		for (Map.Entry<String, Button> sEntry : serviceButtons.entrySet()) {
			service_activation.put(sEntry.getKey(), Boolean.valueOf(sEntry.getValue().getSelection()).toString());
		}
		configuration.setAttribute(AnalyzerAttributes.SERVICE_CHECKBOX_VALUES, service_activation);
		
		configuration.setAttribute(AnalyzerAttributes.EXECUTION_SERVICE_CLASS_NAMES, executionServiceClassNames);
		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override	
	public boolean isValid(ILaunchConfiguration launchConfig) {
		
		// if at least 1 file is specified and if all specified files are valid
		String[] files = this.textTxtIn.getText().split(";");
		boolean filesAreValid = true;
		for (String file_text : files) {
			File file = new File(file_text);

			for (String f : txtFileExtensions) {
				if (!((file.exists()) && (file.getName().endsWith(f.substring(1))))) {
					filesAreValid = false;
				}
			}
		}
		
		// ask Analysis if they are Valid
		boolean analysisAreValid = true;
		for (IAnalysis ana : analysis) {
			if (!ana.isValid()) {
				analysisAreValid = false;
			}
		}
		
		boolean servicesAreValid = true;
		for (NameServiceProvider s : services) {
			if (!s.isValid()) {
				servicesAreValid = false;
			}
		}
		
		// if at least one Analysis is chosen
		boolean atLeast1 = false;
		for (Button b: analysisButtons.values()) {
			if (b.getSelection()) {
				atLeast1 = true;
			}
		}
		
		boolean atLeast1s = false;
		for (Button bs: serviceButtons.values()) {
			if (bs.getSelection()) {
				atLeast1s = true;
			}
		}
		
		return ((files.length > 0) && filesAreValid && analysisAreValid && atLeast1 && servicesAreValid && atLeast1s);
	}
	
	@Override
	protected boolean isDirty() { // TODO
		System.out.println("is dirty");
		return true;
	}

	@Override
	public boolean canSave() { // TODO
		System.out.println("can save");
		return true;
	}
	
}
