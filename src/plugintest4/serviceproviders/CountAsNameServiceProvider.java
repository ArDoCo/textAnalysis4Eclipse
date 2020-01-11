package plugintest4.serviceproviders;

public class CountAsNameServiceProvider implements NameServiceProvider{
	
	@Override
	public String getName() {
		return "Count all As";  
	}

	@Override
	public String getExecutionServiceProviderName() {
		return "CountAsExecutionServiceProvider";
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
