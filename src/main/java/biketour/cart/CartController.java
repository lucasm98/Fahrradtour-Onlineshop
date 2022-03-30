package biketour.cart;

import biketour.bike.*;
import biketour.booking.Booking;
import biketour.booking.BookingManager;
import biketour.purchase.PurchaseManager;
import biketour.user.UserManager;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.SalespointIdentifier;
import org.salespointframework.order.*;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.payment.*;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.money.Monetary;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("cart")
public class CartController {

	@Autowired
	private final BikeManager bikeManager;
	private final PurchaseManager purchaseManager;
	private final BookingManager bookingManager;
	private final UserManager userManager;

	public CartController(BikeManager bikeManager, PurchaseManager purchaseManager,
						  BookingManager bookingManager, UserManager userManager) {
		this.bikeManager = bikeManager;
		this.purchaseManager = purchaseManager;
		this.bookingManager = bookingManager;
		this.userManager=userManager;
	}

	/**
	 * @return new Cart
	 */
	@ModelAttribute("cart")
	Cart initializeCart() {

		System.out.println("CartContoller Initialize Cart");
		return new Cart();
	}

	/**
	 *
	 * @param model will never be null
	 * @param userAccount has to be logged in to see the cart
	 * @param cart is the attribute that saves the products
	 * @return a view of the cart
	 */
	@GetMapping("/cart")
	public String cartGet(@Valid CheckBikeForm form, Model model, @LoggedIn Optional<UserAccount> userAccount,
						  @ModelAttribute Cart cart) {

		System.out.println("CartController get Cart"+cart.get());
		ArrayList<Product> returnlist = new ArrayList<>();
		for(CartItem item : cart.get().collect(Collectors.toCollection(ArrayList::new))){
			returnlist.add(item.getProduct());
		}
		model.addAttribute("bikes",returnlist);
		model.addAttribute("price",cart.getPrice());
		return "cart";
	}


	/**
	 * @param form that has information of the bikes
	 * @param id of all bikes added to the cart
	 * @param cart holds all the products
	 * @param model will never be null
	 * @return the cart view
	 */
	@PostMapping("/cart")
	String checkBikePost(@Valid CheckBikeForm form, @RequestParam("pid")Optional<OrderIdentifier> id,
						 @ModelAttribute Cart cart, Model model){
		if(form.getIDs() == null) {
			return "redirect:/myBooking/";
		}
		ArrayList<Bike> bikes = new ArrayList();
		for(String s:form.getIDs().split(",")){
			Bike bike = bikeManager.findBikeByStringId(s);
			System.out.println("Bike : "+bike.getId()+bike.getBikeStatus());
			if(bike.getBikeStatus()!= Bike.BikeStatus.BOUGHT && cart.get().filter(element
					-> element.getProduct().equals(bike)).count()==0){
				cart.addOrUpdateItem(bike,Quantity.of(1));
				bikes.add(bike);
			}
			System.out.println("Bike : "+bike.getId()+bike.getBikeStatus());

		}
		for(CartItem item : cart.get().collect(Collectors.toCollection(ArrayList::new))){
			if(!bikes.contains((Bike) item.getProduct())){
				bikes.add((Bike) item.getProduct());
			}

		}
		model.addAttribute("bikes", bikes);
		model.addAttribute("price", cart.getPrice());
		return "cart";
	}

	/**
	 * set all the bikes to bought when the buy button is clicked in the cart view
	 * @param userAccount has to be logged in to buy the bikes
	 * @param cart has all the bikes as products saved
	 * @return the cart view
	 */
	@PostMapping("/payment")
	public String buyBikes(@LoggedIn Optional<UserAccount> userAccount,@ModelAttribute Cart cart,
						   Model model,@RequestParam("ids")Optional<String> name,
						   @RequestParam("credits") Optional<String> credits){

		System.out.println("CartController buy Cart"+name.get());
		ArrayList<Bike> ebikes = new ArrayList<>();
		ArrayList<Bike> bikes = new ArrayList<>();
		for(CartItem item : cart.get().collect(Collectors.toCollection(ArrayList::new))){
			Bike bike = bikeManager.findBikeById(item.getProduct().getId()).get();
			bike.setBikeStatus(Bike.BikeStatus.BOUGHT);
			System.out.println("Bike : "+bike.getId()+bike.getBikeStatus());
			if(bike.getBikeType().equals(Bike.BikeType.EBIKE)){
				ebikes.add(bikeManager.findBikeById(bike.getId()).get());
			}else{
				bikes.add(bikeManager.findBikeById(bike.getId()).get());
			}
		}
		int price=0;
		int intCredits = Integer.parseInt(credits.get());
		int point4purchase=intCredits;

		price = (int)((double)cart.getPrice().getNumber().intValue()*(1-(0.1*intCredits)));
		System.out.println("Price"+price+" | "+intCredits);
//		userManager.findUserByUserAccount(userAccount.get()).get().setCreditsUsed(intCredits);


		cart.clear();
		ArrayList<Bike> allBikes = new ArrayList<>();
		allBikes.addAll(ebikes);
		allBikes.addAll(bikes);
		Streamable<Booking> bookings =bookingManager.getBookingsFromUserAccount(userAccount.get());
		SalespointIdentifier bookingId = new SalespointIdentifier() ;
		for(Booking booking : bookings){
			for(Bike bike :allBikes){
				if(booking.getBikes().contains(bike)){
					bookingId =booking.getId();
					break;
				}
			}
		}





		Booking booking=bookingManager.getBookingsById((OrderIdentifier) bookingId);

		purchaseManager.saveBikes(userAccount.get(),name.get().contains("card")? new DebitCard(
			"FlyingCard", "123456789",
				userAccount.get().getFirstname()+" "+userAccount.get().getLastname(),
				userManager.findUserByUserAccount(userAccount.get()).get().getAddress().toString(),
				LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(300),
				"wrhfbwurht547f34", Money.of(100,"EUR") ) :Cash.CASH,
				intCredits,
				booking,
				allBikes
				);

		if(!ebikes.isEmpty()){
			int ebikeprice = ebikes.size()*ebikes.get(0).getPrice().getNumber().intValue();
			System.out.println("MOMOMOMM :"+ebikeprice+" | Size : "+ebikes.size());
			model.addAttribute("eprice",ebikeprice+"");
			model.addAttribute("ecount",ebikes.size()+"");
		}else{
			model.addAttribute("eprice",0);
			model.addAttribute("ecount",0);
		}
		if(!bikes.isEmpty()){
			int bikeprice = bikes.size()*bikes.get(0).getPrice().getNumber().intValue();
			model.addAttribute("bprice",bikeprice+"");
			model.addAttribute("bcount",bikes.size()+"");
			System.out.println("MEMEMEME :"+bikeprice+" | Size : "+bikes.size());
		}else{
			model.addAttribute("bprice",0);
			model.addAttribute("bcount",0);
		}
		model.addAttribute("points",point4purchase);

		model.addAttribute("price",price);

		return "purchase";
	}


}
