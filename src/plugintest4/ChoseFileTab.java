package plugintest4;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
 
import plugintest4.listener.fileopening.OpenWorkspaceButtonListener;
import plugintest4.listener.fileopening.OpenLocalFileSystemButtonListener;

public class ChoseFileTab extends AbstractLaunchConfigurationTab {

	// TODO output definieren können (niedrigere Prio)
	
	private Label helloText;
	private static final String TEXT_LOAD_TXT_FILE = "Load txt File";
	private Text textTxtIn;
	private static final String[] txtFileExtensions = new String[] { "*.txt" };
	private Button loadTxtFileButton;
	private List<String> dummy_text = new ArrayList<>();
	private String dummy_text_text = "This is a dummy text";
	private Button wordCountButton;
	
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		setControl(container);

		GridLayoutFactory.swtDefaults().applyTo(container);

		this.helloText = new Label(container, SWT.LEFT);
		this.helloText.setText("This Run Config lets you analyze text files. "
				+ "Please chose a file here. Analysis can be chosen in the next Tab.");

		final ModifyListener modifyListener = (ModifyEvent e) -> {
			setDirty(true);
			updateLaunchConfigurationDialog();
		};

		final SelectionListener checkboxSelectionListener = new SelectionAdapter () {
	         public void widgetSelected(SelectionEvent event) {
	             //Button button = ((Button) event.widget);
	             //System.out.print(button.getText());
	             //System.out.println(" selected = " + button.getSelection());
	             setDirty(true);
	 			 updateLaunchConfigurationDialog();
	          };
	       };
		
		Shell shell = getShell();

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
        
        this.wordCountButton = new Button (container, SWT.CHECK);
        this.wordCountButton.setText("Count Words");
        this.wordCountButton.addSelectionListener(checkboxSelectionListener);
        
	}


	@Override
	public String getName() {
		return "Data";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		
		try {
			List<String> fileNames = configuration.getAttribute(AnalyzerAttributes.FILE_NAME, dummy_text);
			String fileName = String.join(";", fileNames);
			this.textTxtIn.setText(fileName);
			
			Map<String, String> checkbox_activation = configuration
					.getAttribute(AnalyzerAttributes.CHECKBOX_ACTIVATION, new HashMap<String, String>());
			this.wordCountButton.setSelection(Boolean.valueOf(checkbox_activation.getOrDefault(AnalyzerAttributes.WORD_COUNT, "false")));

		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("perform Apply");
		
		List<String> file_names = Arrays.asList(this.textTxtIn.getText().split(";"));
		configuration.setAttribute(AnalyzerAttributes.FILE_NAME, file_names);
		
		// We have to convert bools to string because attributes can either save only bool or a map of string,string.
		Map<String, String> checkbox_activation = new HashMap<>(); 
		checkbox_activation.put(AnalyzerAttributes.WORD_COUNT, Boolean.valueOf(this.wordCountButton.getSelection()).toString());
		configuration.setAttribute(AnalyzerAttributes.CHECKBOX_ACTIVATION, checkbox_activation);
		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override	
	public boolean isValid(ILaunchConfiguration launchConfig) {
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
		// TODO mind. 1 box gecheckt
		return ((files.length > 0) && filesAreValid);
		
	}
	
	@Override
	protected boolean isDirty() {
		System.out.println("is dirty");
		return true;
	}

	@Override
	public boolean canSave() {
		System.out.println("can save");
		return true;
	}
	
}
