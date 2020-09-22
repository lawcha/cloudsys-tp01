package ch.heiafr.pigroup6.passioncuisine.services;

import ch.heiafr.pigroup6.passioncuisine.model.users.Admin;
import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import ch.heiafr.pigroup6.passioncuisine.model.users.User;
import ch.heiafr.pigroup6.passioncuisine.repository.user.AdminRepository;
import ch.heiafr.pigroup6.passioncuisine.repository.user.ClientRepository;
import ch.heiafr.pigroup6.passioncuisine.repository.user.UserRepository;
import org.springframework.data.convert.ClassGeneratingEntityInstantiator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.channels.ClosedByInterruptException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;

    public UserService(UserRepository userRepository,
                       ClientRepository clientRepository,
                       AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
    }

    public User loginUser(User user) {
        String email = userRepository.login(user.getEmail(), user.getPassword());

        if (email == null || email.isEmpty()) return null;

        return loadUserByUsername(email);
    }

    public Client saveClient(Client client) {
        clientRepository.saveClient(
                client.getEmail(),
                client.getPassword(),
                client.getFirstName(),
                client.getLastName(),
                client.getStreet(),
                client.getPhone(),
                client.getLocation().getNpa(),
                client.getLocation().getCity()
        );
        Optional<Client> addedClient = clientRepository.findById(client.getEmail());
        return addedClient.orElse(null);
    }

    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<? extends User> user = clientRepository.findById(email);

        if (!user.isPresent()) user = adminRepository.findById(email);

        if (!user.isPresent()) throw new UsernameNotFoundException("User or password invalid");
        return user.get();
    }

    // Debug
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
