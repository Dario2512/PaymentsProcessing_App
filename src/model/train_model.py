import pandas as pd
from sklearn.ensemble import IsolationForest
import joblib

# Încarcă datele
df = pd.read_csv('transactions.csv')

# Selectează doar coloanele relevante
X = df[['amount']]

# Creează și antrenează modelul
model = IsolationForest(contamination=0.2, random_state=42)
model.fit(X)

# Salvează modelul într-un fișier .pkl
joblib.dump(model, 'model.pkl')

print("Modelul a fost antrenat și salvat ca model.pkl")