package kornell.cfg

import javax.enterprise.inject.Produces
import javax.persistence.PersistenceContext
import javax.persistence.EntityManager

class CDIResources {
    @Produces
    @PersistenceContext
    var em:EntityManager = null;
}