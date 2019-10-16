package essai;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Service extends ResourceConfig {
	public Service() {
		System.out.println("Chargement de " + this.getClass());
		this.register(LoggingFeature.class);
		this.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");
		this.register(SalutFr.class);
		this.register(SalutEn.class);
	}
}