import java.util.ArrayList;
import java.util.List;

public class ServiceCentral {
    List<Service> services;


    public ServiceCentral() {
        services = new ArrayList<Service>();
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void removeService(Service service) {
        services.remove(service);
    }

    public List<Service> getServices() {
        return services;
    }
}
