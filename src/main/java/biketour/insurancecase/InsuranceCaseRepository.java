package biketour.insurancecase;

import biketour.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

interface InsuranceCaseRepository extends CrudRepository<InsuranceCase, Long> {
	@Override
	Streamable<InsuranceCase> findAll();
}
