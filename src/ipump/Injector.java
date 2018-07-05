package ipump;

 /**
  * Inner class serves exclusively as an intermediary between users and controllers.
  * Injects insulin.
  */
public class Injector {
    Human user;

    /**
	 * Constructor
     *
     * @param user of the appliance
     */
    public Injector(Human user)
    {
            this.user = user;
    }

    /**
	 * Inject insulin into the user.
     *
     * @param puts the dose of insulin
     */
    public void inject(int dose)
    {
            user.setInsulin(dose);
    }
}
