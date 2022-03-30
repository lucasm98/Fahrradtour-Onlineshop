package biketour;

import biketour.bike.Bike;
import biketour.bike.BikeManager;
import biketour.booking.BookingManager;
import biketour.insurancecase.InsuranceCaseManager;
import biketour.material.MaterialManager;
import biketour.purchase.PurchaseManager;
import biketour.tour.ConcreteTour;
import biketour.tour.ConcreteTourCatalog;
import biketour.tour.ConcreteTourManager;
import biketour.tour.TourManager;
import biketour.user.UserManager;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Marcel Körner
 */
@Controller
public class IndexController {

	ConcreteTourCatalog catalog;
	UserManager userManager;
	TourManager tourManager;
	ConcreteTourManager concreteTourManager;
	BookingManager bookingManager;
	PurchaseManager purchaseManager;
	InsuranceCaseManager insuranceCaseManager;
	BikeManager bikeManager;
	MaterialManager materialManager;


	IndexController(MaterialManager materialManager,ConcreteTourCatalog catalog,UserManager userManager,
					TourManager tourManager,ConcreteTourManager concreteTourManager,BookingManager bookingManager,
					PurchaseManager purchaseManager,InsuranceCaseManager insuranceCaseManager,BikeManager bikeManager){
		this.catalog=catalog;
		this.userManager=userManager;
		this.tourManager=tourManager;
		this.concreteTourManager=concreteTourManager;
		this.bookingManager=bookingManager;
		this.purchaseManager=purchaseManager;
		this.insuranceCaseManager=insuranceCaseManager;
		this.bikeManager=bikeManager;
		this.materialManager=materialManager;

	}

	@GetMapping("/")
	public String indexGet(@LoggedIn Optional<UserAccount> userAccount, Model model) {
		concreteTourManager.validateDate();
		System.out.println(userAccount);
		ArrayList<ConcreteTour> list = new ArrayList<>();
		Iterator<ConcreteTour> iterable = catalog.findByStatus(ConcreteTour.Status.ACTIVE).iterator();
		while(iterable.hasNext()) {
			ConcreteTour iter = iterable.next();
			if(list.size()==3){
				if (list.get(0).getLeftDays() > iter.getLeftDays()) {
					list.set(2,list.get(1));
					list.set(1,list.get(0));
					list.set(0,iter);
				}else{
					if (list.get(1).getLeftDays() > iter.getLeftDays()) {
						list.set(2,list.get(1));
						list.set(1,iter);
					}else{
						if (list.get(2).getLeftDays() > iter.getLeftDays()) {
							list.set(2,iter);
						}
					}
				}
			}
			if(list.size()==2){
				if (list.get(0).getLeftDays() > iter.getLeftDays()) {
					list.add(list.get(1));
					list.set(1,list.get(0));
					list.set(0,iter);
				}else{
					if (list.get(1).getLeftDays() > iter.getLeftDays()) {
						list.add(list.get(1));
						list.set(1,iter);
					}else{
						list.add(iter);
					}
				}
			}
			if(list.size()==1){
				if (list.get(0).getLeftDays() > iter.getLeftDays()) {
					list.add(list.get(0));
					list.set(0,iter);
				}else{
					list.add(iter);
				}
			}else{
				if(list.isEmpty()){
					list.add(iter);
				}
			}
		}
		
		model.addAttribute("usergesamt",userManager.findAll().toList().size());
		model.addAttribute("usercoach",userManager.findByRole(Role.of("TOURGUIDE")).toList().size());
		model.addAttribute("useruser",userManager.findByRole(Role.of("CUSTOMER")).toList().size());

		model.addAttribute("tourgesamt", tourManager.getRepository().findAll().spliterator().estimateSize());
		model.addAttribute("tourconcret",concreteTourManager.getConcreteTourCatalog().findAll().toList().size());
		model.addAttribute("touractiv",concreteTourManager.getConcreteTourCatalog().findByStatus(ConcreteTour.
				Status.ACTIVE).spliterator().estimateSize());

		model.addAttribute("insurencecase",insuranceCaseManager.findAll().toList().size());

		model.addAttribute("bookinggesamt",bookingManager.getAllBookings(Pageable.unpaged()).toList().size());
		model.addAttribute("bookingbooked",bookingManager.getBookingsWithOrderStatus(OrderStatus.OPEN).
				toList().size());
		model.addAttribute("bookingpayed",bookingManager.getBookingsWithOrderStatus(OrderStatus.PAID).
				toList().size());
		model.addAttribute("bookingcanceled",bookingManager.getBookingsWithOrderStatus(OrderStatus.CANCELLED).
				toList().size());

		model.addAttribute("bikegesamt",bikeManager.findAll().toList().size());
		model.addAttribute("bikesold",bikeManager.findAll().filter(element -> element.getBikeStatus()==
				Bike.BikeStatus.BOUGHT).toList().size());
		model.addAttribute("bikefree",bikeManager.findAll().filter(element -> element.getBikeStatus()==
				Bike.BikeStatus.AVAILABLE).toList().size());

		model.addAttribute("requests",materialManager.getAllRequests());
		model.addAttribute("activrequests",materialManager.getAllActivRequests());


		if(insuranceCaseManager.findAll().toList().size()<3){
			int i =insuranceCaseManager.findAll().toList().size();
			model.addAttribute("cases",insuranceCaseManager.findAll().toList().subList(0,i));
		}else{
			model.addAttribute("cases",insuranceCaseManager.findAll().toList().subList(0,3));
		}



		model.addAttribute("catalog",list);
		return "index";
	}

	/**
	 * @return the impressum view
	 */
	@GetMapping("/impressum")
	public String impressumGet(){
		return "impressum";
	}


}
