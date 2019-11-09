package infrastructure.jaxrs;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import infrastructure.langage.Types;

@Priority(Priorities.HEADER_DECORATOR + 2)
public class AdapterClientReponsesPUTEnOption implements ReaderInterceptor {

	public AdapterClientReponsesPUTEnOption() {
		System.out.println("** Initialisation du filtre de type " + this.getClass());
	}

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
		if (context.getType().equals(Optional.class)) {
			ParameterizedType genType = (ParameterizedType) context.getGenericType();
			Type actualType = genType.getActualTypeArguments()[0];

			context.setType(Types.convertirTypeEnClasse(actualType));
			return Optional.of(context.proceed());
		} else {
			return context.proceed();
		}
	}
}
