package biketour.user;

import biketour.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Marcel Körner
 */
interface UserRepository extends CrudRepository<User, Long> {

	@Override
	Streamable<User> findAll();

}
