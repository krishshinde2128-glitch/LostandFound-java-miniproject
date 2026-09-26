import java.time.LocalDate;
import java.util.*;
public class LostFoundManager {

    private final ArrayList<Item> items = new ArrayList<>();
    private final HashMap<String, Item> itemMap = new HashMap<>();
    private final TreeMap<String, ArrayList<Item>> categoryIndex = new TreeMap<>();
    private final LinkedList<Claim> claimHistory = new LinkedList<>();
    private final ArrayList<Student> students = new ArrayList<>();
    private final HashMap<String, Student> studentMap = new HashMap<>();
    private final HashMap<String, Claim> claimMap = new HashMap<>();

    private int itemCounter = 1;
    private int claimCounter = 1;

    // ================= VALIDATION HELPERS =================

    private void validateNotBlank(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty.");
        }
    }

    private void validateCategory(String category) throws ValidationException {
        if (!Item.isValidCategory(category)) {
            throw new ValidationException("Invalid category '" + category + "'. Allowed: "
                    + Arrays.toString(Item.CATEGORIES));
        }
    }

    private void validateDate(LocalDate date) throws ValidationException {
        if (date == null) {
            throw new ValidationException("Date cannot be null.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException("Date cannot be in the future.");
        }
    }

    private String generateItemId() {
        return String.format("IT%04d", itemCounter++);
    }

    private String generateClaimId() {
        return String.format("CL%04d", claimCounter++);
    }

    // ================= ITEM REGISTRATION (CREATE) =================

    public LostItem registerLostItem(String description, String category, String location,
                                      LocalDate date, String reporterName, String reporterContact)
            throws ValidationException {
        validateNotBlank(description, "Description");
        validateCategory(category);
        validateNotBlank(location, "Location");
        validateDate(date);
        validateNotBlank(reporterName, "Reporter name");
        validateNotBlank(reporterContact, "Reporter contact");

        String id = generateItemId();
        LostItem item = new LostItem(id, description, category, location, date, reporterName, reporterContact);
        storeItem(item);
        return item;
    }

    public FoundItem registerFoundItem(String description, String category, String location,
                                        LocalDate date, String finderName, String finderContact)
            throws ValidationException {
        validateNotBlank(description, "Description");
        validateCategory(category);
        validateNotBlank(location, "Location");
        validateDate(date);
        validateNotBlank(finderName, "Finder name");
        validateNotBlank(finderContact, "Finder contact");

        String id = generateItemId();
        FoundItem item = new FoundItem(id, description, category, location, date, finderName, finderContact);
        storeItem(item);
        return item;
    }

    private void storeItem(Item item) {
        items.add(item);                      // ArrayList
        itemMap.put(item.getItemId(), item);  // HashMap
        categoryIndex.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item); // TreeMap
    }

    // ================= ITEM CRUD (READ / UPDATE / DELETE) =================

    public Item getItem(String itemId) throws ItemNotFoundException {
        Item item = itemMap.get(itemId);
        if (item == null) {
            throw new ItemNotFoundException("No item found with ID: " + itemId);
        }
        return item;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public void updateItemLocation(String itemId, String newLocation) throws ItemNotFoundException, ValidationException {
        validateNotBlank(newLocation, "Location");
        Item item = getItem(itemId);
        item.location = newLocation;
    }

    public void updateItemDescription(String itemId, String newDescription) throws ItemNotFoundException, ValidationException {
        validateNotBlank(newDescription, "Description");
        Item item = getItem(itemId);
        item.description = newDescription;
    }

    public void deleteItem(String itemId) throws ItemNotFoundException {
        Item item = getItem(itemId);
        items.remove(item);
        itemMap.remove(itemId);
        ArrayList<Item> catList = categoryIndex.get(item.getCategory());
        if (catList != null) {
            catList.remove(item);
            if (catList.isEmpty()) categoryIndex.remove(item.getCategory());
        }
    }

    // ================= STUDENT CRUD =================

    public Student registerStudent(String studentId, String name, String contact, String department)
            throws ValidationException {
        validateNotBlank(studentId, "Student ID");
        validateNotBlank(name, "Name");
        validateNotBlank(contact, "Contact");
        validateNotBlank(department, "Department");
        if (studentMap.containsKey(studentId)) {
            throw new ValidationException("Student ID already exists: " + studentId);
        }
        Student s = new Student(studentId, name, contact, department);
        students.add(s);
        studentMap.put(studentId, s);
        return s;
    }

    public Student getStudent(String studentId) throws ValidationException {
        Student s = studentMap.get(studentId);
        if (s == null) {
            throw new ValidationException("No student found with ID: " + studentId);
        }
        return s;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // ================= SEARCHING =================

    public Item searchById(String itemId) throws ItemNotFoundException {
        return getItem(itemId);
    }

    public List<Item> searchByCategory(String category) {
        ArrayList<Item> result = categoryIndex.get(category);
        return result == null ? new ArrayList<>() : new ArrayList<>(result);
    }

    public List<Item> searchByLocation(String locationKeyword) {
        List<Item> result = new ArrayList<>();
        String key = locationKeyword.toLowerCase();
        for (Item item : items) {
            if (item.getLocation().toLowerCase().contains(key)) {
                result.add(item);
            }
        }
        return result;
    }

    // ================= SORTING =================

    public List<Item> sortByDate() {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(Item::getDate));
        return sorted;
    }

    public List<Item> sortByCategory() {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(Item::getCategory));
        return sorted;
    }

    public List<Item> sortByStatus() {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(Item::getStatus));
        return sorted;
    }

    // ================= CLAIM MANAGEMENT =================

    public Claim fileClaim(String itemId, String studentId) throws ItemNotFoundException, InvalidClaimException, ValidationException {
        Item item = getItem(itemId);
        Student student = getStudent(studentId);

        if (!"FOUND".equals(item.getStatus())) {
            throw new InvalidClaimException("Item " + itemId + " is not available to claim (status: " + item.getStatus() + ").");
        }

        String claimId = generateClaimId();
        Claim claim = new Claim(claimId, itemId, student, LocalDate.now());
        claimHistory.add(claim);      // LinkedList (history log)
        claimMap.put(claimId, claim);
        item.setStatus("CLAIMED");
        return claim;
    }

    public void verifyOwnership(String claimId) throws InvalidClaimException {
        Claim claim = claimMap.get(claimId);
        if (claim == null) {
            throw new InvalidClaimException("No claim found with ID: " + claimId);
        }
        if (claim.isReturned()) {
            throw new InvalidClaimException("Claim " + claimId + " has already been completed.");
        }
        claim.setVerified(true);
    }

    public void processReturn(String claimId) throws InvalidClaimException, ItemNotFoundException {
        Claim claim = claimMap.get(claimId);
        if (claim == null) {
            throw new InvalidClaimException("No claim found with ID: " + claimId);
        }
        if (!claim.isVerified()) {
            throw new InvalidClaimException("Cannot return item before ownership is verified (claim " + claimId + ").");
        }
        if (claim.isReturned()) {
            throw new InvalidClaimException("Claim " + claimId + " has already been marked returned.");
        }
        Item item = getItem(claim.getItemId());
        item.setStatus("RETURNED");
        claim.setReturned(true);
    }

    public List<Claim> getClaimHistory() {
        return new ArrayList<>(claimHistory);
    }

    public List<Claim> getClaimsForItem(String itemId) {
        List<Claim> result = new ArrayList<>();
        for (Claim c : claimHistory) {
            if (c.getItemId().equals(itemId)) result.add(c);
        }
        return result;
    }

    // ================= REPORTS =================

    public Map<String, Long> reportCountByStatus() {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (String s : Item.STATUSES) counts.put(s, 0L);
        for (Item item : items) {
            counts.merge(item.getStatus(), 1L, Long::sum);
        }
        return counts;
    }

    public Map<String, Integer> reportCountByCategory() {
        Map<String, Integer> counts = new TreeMap<>();
        for (Map.Entry<String, ArrayList<Item>> e : categoryIndex.entrySet()) {
            counts.put(e.getKey(), e.getValue().size());
        }
        return counts;
    }

    public int totalItems() { return items.size(); }
    public int totalClaims() { return claimHistory.size(); }
    public int totalStudents() { return students.size(); }
}
