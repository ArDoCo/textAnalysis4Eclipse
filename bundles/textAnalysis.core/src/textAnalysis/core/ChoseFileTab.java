package textAnalysis.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import textAnalysis.core.listener.fileopening.OpenLocalFileSystemButtonListener;
import textAnalysis.provider.AProvider;

public class ChoseFileTab extends AbstractLaunchConfigurationTab {

    // TODO (3) output folder definieren koennen (niedrigere Prio)
    // TODO (2) choose all, choose non button
	// TODO (2) coole Analysen: 
    // stanford core nlp und dann sÔøΩtze aufsplitten, wÔøΩrter pro satz, warnung bei > 24
    // regelwerk von "den sophisten" fÔøΩr das Schreiben von requirements
    // -> mal noch in swt2 folien schauen
    // oder txt mit dictionary einbinden und nach schreibfehlern schauen
    // fortgeschrittener: language tool


    private Label helloText;
    private static final String TEXT_LOAD_TXT_FILE = "Load txt File";
    private Text textTxtIn;
    private static final String[] txtFileExtensions = new String[] { "*.txt" };
   // private Button loadTxtFileButton;
    private List<String> dummy_text = new ArrayList<>();
    private String dummy_text_text = "This is a dummy text";

    private Map<String, Button> analysisButtons;
    private List<AProvider> analysisList;


    public ChoseFileTab() {
        analysisList = new LinkedList<>();
        analysisButtons = new HashMap<>();
    }

    @Override
    public void createControl(Composite parent) {
    	
        // ---------- Setup Tab
        final Composite container = new Composite(parent, SWT.NONE);
        setControl(container);
        Shell shell = getShell();

        GridLayoutFactory.swtDefaults()
                         .applyTo(container);

        helloText = new Label(container, SWT.LEFT);
        helloText.setText("This Run Config lets you analyze text files. "
                + "Please chose a file here. Analysis can be chosen in the next Tab.");

        // ---------- Create Listeners
        final ModifyListener modifyListener = (ModifyEvent e) -> {
            setDirty(true);
            updateLaunchConfigurationDialog();
        };

        final SelectionListener checkboxSelectionListener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                setDirty(true);
                updateLaunchConfigurationDialog();
            }
        };

        // ---------- Load Input Files
        textTxtIn = new Text(container, SWT.SINGLE | SWT.BORDER);
        textTxtIn.setToolTipText("Input txt file(s) [*.txt]");
        textTxtIn.setText(dummy_text_text);
        GridData gd1 = new GridData(800, 30);
        textTxtIn.setLayoutData(gd1);
        textTxtIn.addModifyListener(modifyListener);

        // TODO FIXME ?
        SelectionListener localFileSystemListener = 
        		new OpenLocalFileSystemButtonListener(textTxtIn, txtFileExtensions,
                TEXT_LOAD_TXT_FILE, shell, true);

        final Button localFileSystemButton = new Button(container, SWT.NONE);
        localFileSystemButton.setText("Chose txt file...");
        localFileSystemButton.addSelectionListener(localFileSystemListener);

       
        // ----------- Load Analysis from Service Providers
		String folder;
		try {
			folder = AnalyzerAttributes.getAnalysisSrcFolder();
		
			if (folder.equals("")) {
				addRedLabel("No Folder is specified in <user.dir>/.textanalysisconfig for Analysis jars.", 
						container);
			} else {
				List<AProvider> serviceProviders = AnalyzerAttributes.loadServices(folder);

				if (serviceProviders.size() == 0) {
			    	addRedLabel("No Analysis found.", container);
			    } else {
		    		for (AProvider analysis : serviceProviders) {
		    			Button b1 = new Button(container, SWT.CHECK);
						b1.setText(analysis.getName());
						b1.addSelectionListener(checkboxSelectionListener);
						this.analysisButtons.put(analysis.getName(), b1);
						this.analysisList.add(analysis);
		    		}
			    }
			}
		} catch (IOException e1) { 
			addRedLabel("IOException in Folder Loading. Ask a smart Developer for help!", container);
			e1.printStackTrace();
		}
    }

    /***
     * Adds a red Label (aka a Warning) to the Tab
     * @param message The message of the Label / Warning
     * @param container The container of the tab
     */
    private void addRedLabel(String message, Composite container){
    	Label noFolderText = new Label(container, SWT.LEFT);
		noFolderText.setText(message);
		noFolderText.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
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
            textTxtIn.setText(fileName);

            // ---- Analysis-Services
            Map<String, String> service_checkbox_values = configuration.getAttribute(
                    AnalyzerAttributes.CHECKBOX_ACTIVATION, new HashMap<String, String>());
            for (Map.Entry<String, String> service_entry : service_checkbox_values.entrySet()) {
                analysisButtons.get(service_entry.getKey())
                              .setSelection(Boolean.valueOf(service_entry.getValue()));
                // TODO (1) abfangen wenn jetzt nicht die gleichen Analysen geladen werden als beim letzten mal
                // bzw geladene mit gecheckten abgleichen. Alte wegschmeiﬂen. Oder error schmeiﬂen?
            }

        } catch (CoreException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        System.out.println("perform Apply");

        // ---- Files
        List<String> file_names = Arrays.asList(textTxtIn.getText().split(";"));
        configuration.setAttribute(AnalyzerAttributes.FILE_NAME, file_names);

        // ---- Analysis
        // We have to convert bools to string because attributes can either save only bool or a map of string,string.
        Map<String, String> checkbox_activation = new HashMap<>();

        for (Map.Entry<String, Button> entry : analysisButtons.entrySet()) {
            checkbox_activation.put(entry.getKey(), 
            		Boolean.valueOf(entry.getValue().getSelection()).toString());
        }
        configuration.setAttribute(AnalyzerAttributes.CHECKBOX_ACTIVATION, checkbox_activation);

        // ---- Services
//        Map<String, String> service_activation = new HashMap<>();
//
//        for (Map.Entry<String, Button> sEntry : serviceButtons.entrySet()) {
//            service_activation.put(sEntry.getKey(), Boolean.valueOf(sEntry.getValue()
//                                                                          .getSelection())
//                                                           .toString());
//        }
//        configuration.setAttribute(AnalyzerAttributes.SERVICE_CHECKBOX_VALUES, service_activation);
//
//        configuration.setAttribute(AnalyzerAttributes.EXECUTION_SERVICE_CLASS_NAMES, executionServiceClassNames);

    }

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isValid(ILaunchConfiguration launchConfig) {

        // if at least 1 file is specified and if all specified files are valid
        String[] files = textTxtIn.getText().split(";");
        boolean filesAreValid = true;
        for (String file_text : files) {
            File file = new File(file_text);

            for (String f : txtFileExtensions) {
                if (!((file.exists()) && (file.getName()
                                              .endsWith(f.substring(1))))) {
                    filesAreValid = false;
                }
            }
        }

        // ask Analysis if they are Valid
        boolean analysisAreValid = true;
         for (AProvider ana : analysisList) {
	         	if (!ana.isValid()) {
	         analysisAreValid = false;
	         }
         }

        boolean servicesAreValid = true;
        // for (NameServiceProvider s : services) { TODO
        // if (!s.isValid()) {
        // servicesAreValid = false;
        // }
        // }

        // if at least one Analysis is chosen
        boolean atLeast1 = false;
        for (Button b : analysisButtons.values()) {
            if (b.getSelection()) {
                atLeast1 = true;
            }
        }

//        boolean atLeast1s = false;
//        for (Button bs : serviceButtons.values()) {
//            if (bs.getSelection()) {
//                atLeast1s = true;
//            }
//        }

        return ((files.length > 0) && filesAreValid && analysisAreValid && atLeast1 && servicesAreValid);
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
