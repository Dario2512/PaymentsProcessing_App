import sys
import json

def detect_anomalies(transactions):
    # Exemplu simplu: detectează tranzacții cu amount > 10000
    anomalies = [t for t in transactions if t.get("amount", 0) > 10000]
    return anomalies

def main():
    # Citește tot inputul JSON de pe stdin
    input_data = sys.stdin.read()
    transactions = json.loads(input_data)

    anomalies = detect_anomalies(transactions)

    # Scrie în stdout rezultatul JSON
    print(json.dumps(anomalies))

if __name__ == "__main__":
    main()