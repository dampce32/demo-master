package top.linyisong.model;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.junit.Test;

public class FailFastTest {

	@Test
	public void testFailFast() {
		ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
		        .configure()
		        .failFast( true )
		        .buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		
		Car car = new Car(null, "D", 1);
		Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
		assertEquals(1, constraintViolations.size());
	}
	
	@Test
	public void testNotFailFast() {
		ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
		        .configure()
		        .failFast(false)
		        .buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		
		Car car = new Car(null, "D", 1);
		Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
		assertEquals(3, constraintViolations.size());
	}
}
