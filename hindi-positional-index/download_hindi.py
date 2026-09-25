from datasets import load_dataset

dataset = load_dataset(
    "ai4bharat/IndicCorpV2",
    split="hin_Deva",
    streaming=True
)

count = 0

with open("data/hindi_10k.txt", "w", encoding="utf-8") as f:

    for row in dataset:

        text = row["text"].strip()

        if text:
            f.write(text + "\n")
            count += 1

        if count == 10000:
            break

print(f"Saved {count} Hindi sentences.")
