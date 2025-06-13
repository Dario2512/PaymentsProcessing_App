import pandas as pd
import random
from datetime import datetime, timedelta

def generate_synthetic_transactions(num_normal=300, num_anomalies=30):
    transactions = []

    # Generează tranzacții normale
    for _ in range(num_normal):
        amount = round(random.uniform(5, 200), 2)  # Sume normale
        hour = random.choice(range(8, 22))  # Ore normale (8 AM – 10 PM)
        minute = random.randint(0, 59)
        second = random.randint(0, 59)
        timestamp = datetime(2025, 6, random.randint(1, 25), hour, minute, second)
        card_number = random.choice([1111222233334444, 1234567812345670, 5555666677778888])

        transactions.append({
            "cardNumber": card_number,
            "amount": amount,
            "timestamp": timestamp.isoformat()
        })

    # Generează anomalii
    for _ in range(num_anomalies):
        amount = round(random.uniform(1000, 50000), 2)  # Sume mari
        hour = random.choice([0, 1, 2, 3, 4])  # Ore suspecte (noaptea)
        minute = random.randint(0, 59)
        second = random.randint(0, 59)
        timestamp = datetime(2025, 6, random.randint(1, 25), hour, minute, second)
        card_number = random.choice([1111222233334444, 1234567812345670])

        transactions.append({
            "cardNumber": card_number,
            "amount": amount,
            "timestamp": timestamp.isoformat()
        })

    # Salvează într-un CSV
    df = pd.DataFrame(transactions)
    df.to_csv('transactions.csv', index=False)
    print("transactions.csv has been generated.")

if __name__ == "__main__":
    generate_synthetic_transactions()
