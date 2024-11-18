import google.generativeai as genai
import pandas as pd
import time
from tenacity import retry, stop_after_attempt, wait_fixed
import json

genai.configure(api_key='XXXXXXXXXXXXXXXX')  


def save_progress_to_file(data, filename):
    with open(filename, "w") as f:
        json.dump(data, f)

def load_progress_from_file(filename):
    try:
        with open(filename, "r") as f:
            return json.load(f)
    except FileNotFoundError:
        return []


@retry(stop=stop_after_attempt(3), wait=wait_fixed(5))  
def generate_questions_for_marks(topic, marks):
    prompt = f"List all standard {marks}-mark questions on the topic '{topic}'. Provide only the questions in the format: 1. question, 2. question, ..."
    
    response = genai.GenerativeModel("gemini-1.5-flash").generate_content(prompt)
    response_text = response.text.strip()
    
    questions = []
    questions_list = response_text.split("\n")
    for question in questions_list:
        if question.strip():  
            question_text = question.split(".", 1)[1].strip() if "." in question else question.strip()
            questions.append({
                "MARKS": marks,
                "QUESTION": question_text
            })
    
    time.sleep(2)  
    return questions


def generate_and_save_questions_from_csv(syllabus_file, output_file, progress_file="progress.json"):
 
    prompts_and_marks = [2, 6, 7, 8, 14]

    
    unit_to_co = {
        "UNIT I": "CO1",
        "UNIT II": "CO2",
        "UNIT III": "CO3",
        "UNIT IV": "CO4",
        "UNIT V": "CO5"
    }

    syllabus_df = pd.read_csv(syllabus_file)
    all_questions = load_progress_from_file(progress_file)  
    processed_topics = {q["TOPIC"] for q in all_questions}  

  
    for _, row in syllabus_df.iterrows():
        unit = row["UNIT"]  
        co = unit_to_co.get(unit, "CO1")  
        course_code = row["COURSE_CODE"]  
        topics = row["TOPICS"].split(" - ")  

        for topic in topics:
            if topic in processed_topics:
                continue  
            
            for marks in prompts_and_marks:
                try:
                    
                    questions = generate_questions_for_marks(topic, marks)
                    for question in questions:
                        question["TOPIC"] = topic
                        question["UNIT"] = unit
                        question["CO"] = co
                        question["COURSE_CODE"] = course_code
                    all_questions.extend(questions)
                    processed_topics.add(topic)
                    
                    save_progress_to_file(all_questions, progress_file)
                except Exception as e:
                    print(f"Error generating {marks}-mark questions for topic '{topic}': {e}")

  
    df = pd.DataFrame(all_questions)
    df.to_excel(output_file, index=False)

    print(f"Questions generated and saved to {output_file}")


syllabus_file = "/content/syllabus.csv"  
output_file = "Generated_Questions_All_Marks.xlsx"  
generate_and_save_questions_from_csv(syllabus_file, output_file)
