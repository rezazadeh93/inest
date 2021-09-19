package ink.nest.inest.service;

import ink.nest.inest.domain.SocialName;
import ink.nest.inest.repository.SocialNameRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SocialNameCrudServiceImpl implements SocialNameCrudService {
    private final SocialNameRepository repository;

    public SocialNameCrudServiceImpl(SocialNameRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<String> getAll() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(SocialName::getName)
                .collect(Collectors.toSet());
    }
}
