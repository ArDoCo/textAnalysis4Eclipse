package plugintest4.serviceproviders;

public interface NameServiceProvider {
	
	// Either load Execution Service Provider via Name-Matching or via Specified Class. I guess both is equally ugly. 
	// The later needs less computation.

	public String getName();
	
	public String getExecutionServiceProviderName();

	public boolean isValid();
}
