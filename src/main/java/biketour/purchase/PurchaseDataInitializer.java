package biketour.purchase;

import biketour.purchase.Purchase;
import com.mysema.commons.lang.Assert;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import javax.money.Monetary;

@Component
public class PurchaseDataInitializer implements DataInitializer {

	private OrderManager purchaseManager;
	private UserAccountManager userAccountManager;


	PurchaseDataInitializer(OrderManager<Purchase> purchaseManager, UserAccountManager userAccountManager){

		Assert.notNull(purchaseManager, "OrderManager must not be null!");
		Assert.notNull(userAccountManager, "UserAccount must not be null!");

		this.purchaseManager = purchaseManager;
		this.userAccountManager = userAccountManager;
	}


	@Override
	public void initialize() {

//		UserAccount userAccount = userAccountManager.create("Max",
//				Password.UnencryptedPassword.of("test"), Role.of("USER"));
//
//		Purchase order = new Purchase(userAccount, Cash.CASH);
//		Product bike = new Product("bike_001", Money.of(149, Monetary.getCurrency("EUR")));
//		order.addOrderLine(bike, Quantity.of(1));
//
//		Purchase order2 = new Purchase(userAccount, Cash.CASH);
//		Product bike2 = new Product("bike_002", Money.of(139, Monetary.getCurrency("EUR")));
//		order2.addOrderLine(bike2, Quantity.of(1));
//
//		Purchase order3 = new Purchase(userAccount, Cash.CASH);
//		Product bike3 = new Product("bike_003", Money.of(159,Monetary.getCurrency("EUR")));
//		order3.addOrderLine(bike3, Quantity.of(1));
//
//		purchaseManager.save(order);
//		purchaseManager.save(order2);
//		purchaseManager.cancelOrder(order2);
//		purchaseManager.save(order3);
//		purchaseManager.payOrder(order3);
//
//		UserAccount userAccount2 = userAccountManager.create("Karsten",
//				Password.UnencryptedPassword.of("test2"), Role.of("USER"));
//
//		Purchase order4 = new Purchase(userAccount2, Cash.CASH);
//		Product ebike4 = new Product("bike_004", Money.of(899, Monetary.getCurrency("EUR")));
//		order4.addOrderLine(ebike4, Quantity.of(1));
//
//		Purchase order5 = new Purchase(userAccount2, Cash.CASH);
//		Product ebike5 = new Product("bike_005", Money.of(799, Monetary.getCurrency("EUR")));
//		order5.addOrderLine(ebike5, Quantity.of(1));
//
//		Purchase order6 = new Purchase(userAccount2, Cash.CASH);
//		Product ebike6 = new Product("bike_006", Money.of(759,Monetary.getCurrency("EUR")));
//		order6.addOrderLine(ebike6, Quantity.of(1));
//
//		purchaseManager.save(order4);
//		purchaseManager.save(order5);
//		purchaseManager.cancelOrder(order5);
//		purchaseManager.save(order6);
//		purchaseManager.payOrder(order6);

	}
}