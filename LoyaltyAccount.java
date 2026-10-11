package java_project;

public class LoyaltyAccount {
	
    private String loyaltyCardNumber;
    private int points;

    public LoyaltyAccount(String loyaltyCardNumber, int points) {
        this.loyaltyCardNumber = loyaltyCardNumber;
        this.points = points;
    }

    public String getLoyaltyCardNumber() {
        return loyaltyCardNumber;
    }

    public void setLoyaltyCardNumber(String loyaltyCardNumber) {
        this.loyaltyCardNumber = loyaltyCardNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(double amountPaid) {
        this.points += (int) amountPaid;
    }

    public boolean canRedeem(int pointsToRedeem) {
        return pointsToRedeem >= 50 && points >= pointsToRedeem;
    }

    public double getDiscountFromPoints(int pointsToRedeem) {
        return pointsToRedeem / 10.0;
    }

    public double redeemPoints(int pointsToRedeem) {
        if (canRedeem(pointsToRedeem)) {
            points -= pointsToRedeem;
            return getDiscountFromPoints(pointsToRedeem);
        }
        return 0;
    }
}