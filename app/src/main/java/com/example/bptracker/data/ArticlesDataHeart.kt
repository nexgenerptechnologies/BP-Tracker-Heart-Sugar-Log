package com.example.bptracker.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.bptracker.theme.*

val heartArticles = listOf(
    Article(
        title = "Top 10 Habits for a Healthy Heart",
        content = """
## Building a Strong Foundation
Your heart is a muscle, and exactly like any other muscle in your body, it needs the right care and conditioning to stay strong and efficient. Adopting a heart-healthy lifestyle doesn't have to mean completely overhauling your life overnight. Small, consistent daily habits can make a massive difference over time.

Here are the absolute best habits to protect your cardiovascular health:

- **Move Daily:** Aim for 30 minutes of aerobic activity (walking, cycling, swimming) most days of the week. You don't have to run a marathon; a brisk walk is incredibly effective.
- **Eat the Rainbow:** Fill half your plate with colorful fruits and vegetables. They are packed with natural antioxidants that protect your blood vessels from damage.
- **Prioritize Sleep:** Lack of sleep increases blood pressure and stress hormones. Aim for 7 to 9 hours of quality, uninterrupted sleep every single night.
- **Manage Stress:** Chronic stress raises your heart rate and blood pressure constantly. Practice deep breathing, meditation, or yoga.
- **Ditch the Tobacco:** Smoking is one of the worst things you can possibly do for your heart. Quitting reduces your risk of heart disease almost immediately, and within a few years, your risk drops to that of a non-smoker.
- **Watch Your Salt:** High sodium intake leads to fluid retention and high blood pressure. Read food labels carefully!
- **Limit Alcohol:** Drinking too much alcohol can raise your blood pressure and add empty calories that lead to weight gain.
- **Know Your Numbers:** Regularly check your blood pressure, cholesterol, and blood sugar levels. Knowledge is power.
- **Stay Hydrated:** Drinking enough water helps your heart pump blood more easily through the blood vessels to the muscles.
- **Maintain a Healthy Weight:** Carrying extra weight forces your heart to work harder to supply oxygen to your body. Losing just a few pounds drastically reduces the strain on your cardiovascular system.
        """.trimIndent(),
        color = HighOrange,
        icon = Icons.Default.Favorite,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Early Warning Signs of Heart Disease",
        content = """
## Don't Ignore the Clues
Heart disease is often thought of as a sudden, catastrophic event, like a heart attack out of nowhere. However, cardiovascular disease typically develops slowly over many years, often presenting subtle warning signs long before a major emergency occurs. Recognizing these early symptoms can save your life.

### Chest Discomfort
This is the most common and classic sign of heart danger. 
- It might feel like pressure, tightness, or a heavy squeezing sensation in the center of your chest. 
- It can happen when you're resting or doing physical activity. 
- **Important:** If this feeling lasts for more than a few minutes, or radiates to your arm, neck, or jaw, seek emergency medical help immediately.

### Shortness of Breath
If you find yourself gasping for air after mild exertion—like walking up a single flight of stairs, carrying groceries, or even just lying flat in bed—when you previously had no trouble doing so, it could be a sign that your heart is failing to pump blood effectively to your lungs.

### Unexplained Fatigue
Feeling exceptionally tired, especially for women, can be a symptom of heart failure. If you are constantly exhausted and normal daily activities leave you wiped out for no obvious reason, your heart may be struggling to deliver enough oxygen-rich blood to your body tissues.

### Swelling in Legs and Ankles
When your heart isn't pumping fast or strong enough, blood backs up in the veins, causing fluid to leak into surrounding tissues. 
- This often manifests as swelling (edema) in the feet, ankles, and lower legs. 
- If you notice your shoes are suddenly too tight, or you leave deep indentations when you take off your socks, it warrants a checkup with a cardiologist.
        """.trimIndent(),
        color = ElevatedYellow,
        icon = Icons.Default.Warning,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "How High Blood Pressure Affects Heart Health",
        content = """
## The Silent Destroyer
High blood pressure (hypertension) is frequently called the 'silent killer' because it damages your entire cardiovascular system over time without presenting any noticeable symptoms. Understanding the physical mechanics of this damage is crucial for taking it seriously and adhering to your medication regimen.

### The Overworked Muscle
Think of your heart as a pump and your arteries as the pipes. High blood pressure means the pressure inside those pipes is much too high. 
- To pump blood against this intense resistance, your heart muscle has to work much harder than it normally should. 
- Over time, just like any over-worked muscle at the gym, the heart muscle thickens (a condition called left ventricular hypertrophy). 
- A thickened heart muscle is stiffer and far less effective at pumping blood, which can eventually lead to congestive heart failure.

### Damaged Arteries
Healthy arteries are incredibly flexible, strong, and smooth on the inside. High blood pressure acts like rough sand rushing through a pipe. The immense pressure causes microscopic tears in the delicate inner lining of your blood vessels. 

### The Plaque Trap
Once the artery lining is damaged, fats and cholesterol circulating in your blood easily get trapped in those tears. 
- Over the years, this buildup forms hard plaque, which narrows the arteries (atherosclerosis). 
- If a piece of this brittle plaque breaks off, a blood clot can rapidly form around it, completely blocking blood flow to the heart (causing a massive heart attack) or the brain (causing a stroke).
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Info,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Cholesterol and Heart Health: What You Need to Know",
        content = """
## Understanding the Wax
Cholesterol is a waxy, fat-like substance found in all the cells in your body. Your body actually needs some cholesterol to make hormones, vitamin D, and substances that help you digest foods. However, having too much of the wrong type of cholesterol circulating in your bloodstream is a major risk factor for heart disease.

### The "Bad" Cholesterol: LDL
Low-density lipoprotein (LDL) is widely known as the "bad" cholesterol. 
- If you have too much LDL in your blood, it can combine with other substances to form thick, hard plaque on the inner walls of your arteries. 
- This plaque buildup narrows the arteries, drastically reducing blood flow. If a plaque ruptures, a blood clot can form, potentially leading to a fatal heart attack or stroke.

### The "Good" Cholesterol: HDL
High-density lipoprotein (HDL) is known as the "good" cholesterol. 
- HDL acts like a microscopic scavenger, picking up excess bad cholesterol in your blood and taking it back to your liver, where it's safely broken down and removed from your body. 
- Higher levels of HDL are associated with a significantly lower risk of heart disease.

### Managing Your Levels
You can dramatically improve your cholesterol profile through a healthy diet. 
- **Avoid:** Reduce your intake of saturated fats (found in red meat and full-fat dairy) and completely eliminate artificial trans fats (found in some fried and commercially baked goods). 
- **Consume:** Increase your intake of soluble fiber (like oatmeal, beans, and apples) and foods rich in omega-3 fatty acids (like salmon, flaxseed, and walnuts) to naturally boost your HDL and lower your LDL.
        """.trimIndent(),
        color = NormalGreen,
        icon = Icons.Default.Lightbulb,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "The Connection Between Diabetes and Heart Disease",
        content = """
## A Dangerous Duo
If you have diabetes, your risk of developing heart disease is significantly higher than someone without the condition. In fact, heart disease is the leading cause of death for people with diabetes. Understanding the biological connection between blood sugar and the heart is the first step in prevention.

### The Danger of Excess Sugar
Over time, high blood glucose from uncontrolled diabetes can severely damage the blood vessels and the delicate nerves that control your heart. 
- The excess sugar in your bloodstream acts almost like a corrosive agent, slowly degrading the structural integrity of your vascular system and making your arteries stiff and brittle.

### The Co-Conspirators
People with diabetes often have other co-existing conditions that further exponentially increase their risk for heart disease. These include:
- **High Blood Pressure:** This forces the heart to work harder and physically damages arteries.
- **Abnormal Cholesterol:** People with diabetes often have lower HDL (good) cholesterol and higher LDL (bad) cholesterol, along with high triglycerides. This combination is highly atherogenic (plaque-forming).
- **Obesity:** Excess weight makes it much harder to manage blood sugar and increases cardiac strain.

### Taking Control
The most effective way to protect your heart if you have diabetes is to strictly manage your blood sugar levels (keeping your A1C tightly in your target range). Combining this with daily blood pressure monitoring, cholesterol management, and a strict heart-healthy diet can drastically reduce your risk of cardiovascular complications and allow you to live a long, healthy life.
        """.trimIndent(),
        color = HighOrange,
        icon = Icons.Default.Warning,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Understanding Heart Rate: What's Normal and What's Not?",
        content = """
## Listening to Your Pulse
Your heart rate, or pulse, is the number of times your heart beats per minute. It is a vital sign that provides a direct window into your overall cardiovascular fitness and autonomic nervous system health. Checking it regularly is a great habit.

### Resting Heart Rate
For most adults, a normal resting heart rate ranges from **60 to 100 beats per minute (bpm)**. 
- Generally, a lower resting heart rate implies more efficient heart function and better cardiovascular fitness. 
- For example, a well-trained athlete might have a normal resting heart rate closer to 40 or 50 beats per minute because their heart muscle is so strong it doesn't need to beat as often to supply the body with blood.

### When to Be Concerned
While a resting heart rate between 60 and 100 is technically considered "normal," studies suggest that a resting heart rate consistently at the higher end of that spectrum (above 85 bpm) may indicate poor fitness and increase the risk of cardiovascular events over time. 
- If your resting heart rate is consistently above 100 bpm (tachycardia).
- If you are not a trained athlete and your resting heart rate is below 60 bpm (bradycardia)—especially if you're experiencing other symptoms like fainting, dizziness, or shortness of breath—you should consult a doctor immediately.

### How to Check Your Pulse
You can easily check your pulse on the inside of your wrist, just below your thumb. Lightly press the index and middle fingers of your other hand on the artery. Count the beats for exactly 30 seconds and multiply by two to get your beats per minute. Do not use your thumb to check your pulse, as it has a pulse of its own that can confuse the reading.
        """.trimIndent(),
        color = ElevatedYellow,
        icon = Icons.Default.Favorite,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Stress and Heart Health: How They Are Connected",
        content = """
## The Mind-Heart Connection
In today's fast-paced, always-on world, stress is completely unavoidable. But chronic, unmanaged stress is terrible for your cardiovascular system. The connection between your mind and your heart is biological, immediate, and profound.

### The Fight or Flight Response
When you experience stress (like missing a deadline or getting into an argument), your body releases a massive surge of hormones, including adrenaline and cortisol. This is the ancient "fight or flight" response. 
- Adrenaline causes your heart to beat faster and your blood pressure to spike temporarily to prepare your body for physical action. 
- If you are chronically stressed every day, your body is constantly in this heightened state of alarm, forcing your heart to work overtime continuously and damaging your blood vessels.

### Indirect Damage
Stress also harms your heart indirectly by encouraging unhealthy coping mechanisms. When highly stressed, people are much more likely to smoke cigarettes, drink excessive alcohol, overeat (especially high-fat, high-sugar comfort foods), and skip exercise. All of these behaviors independently skyrocket your risk of heart disease.

### Breaking the Cycle
Learning to consciously manage stress is just as important for your heart as eating right and exercising. 
- Techniques like mindfulness meditation, yoga, deep breathing exercises, and spending time in nature have been clinically proven to lower blood pressure and reduce resting heart rates. 
- Don't hesitate to seek professional counseling or therapy if you feel overwhelmed by chronic stress. Protecting your mental health is protecting your heart.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Info,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Heart-Healthy Diet: Foods to Eat and Avoid",
        content = """
## Fueling the Engine
Your diet is your first and best line of defense against heart disease. What you put on your plate dictates your cholesterol levels, blood pressure, and inflammation levels. Adopting a Mediterranean or DASH-style diet is considered the gold standard for cardiac health by cardiologists worldwide.

### Foods to Prioritize
- **Omega-3 Fatty Acids:** Found abundantly in fatty fish (salmon, mackerel, sardines), walnuts, and flaxseeds. Omega-3s reduce inflammation and lower the risk of dangerous arrhythmias and plaque buildup.
- **Whole Grains:** Oats, quinoa, brown rice, and whole-wheat bread are packed with dietary fiber, which helps sweep "bad" cholesterol out of your digestive system before it enters your blood.
- **Berries:** Blueberries, strawberries, and blackberries are rich in antioxidants that protect the delicate lining of your blood vessels.
- **Leafy Greens:** Spinach, Swiss chard, and kale are high in vitamin K and nitrates, which protect arteries, promote proper blood clotting, and lower blood pressure.

### Foods to Strictly Limit or Avoid
- **Saturated and Trans Fats:** Avoid highly processed meats (bacon, sausage, hot dogs), full-fat dairy, and anything containing partially hydrogenated oils. These directly increase your bad cholesterol and clog arteries.
- **Added Sugars:** Sugary sodas, energy drinks, pastries, and candies contribute to obesity, diabetes, and massive systemic inflammation.
- **Excessive Sodium:** Heavily salted foods cause your body to retain water, forcing your heart to pump much harder. Rely on fresh herbs, garlic, and spices for flavor instead of the salt shaker.
        """.trimIndent(),
        color = NormalGreen,
        icon = Icons.Default.Lightbulb,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "The Benefits of Regular Exercise for Heart Health",
        content = """
## Training Your Heart
The heart is a muscle, and exactly like your biceps or quadriceps, it becomes stronger, larger, and much more efficient when it is regularly challenged through exercise. Physical inactivity is one of the leading risk factors for heart disease globally, but it is completely reversible at any age.

### Cardiovascular Efficiency
When you engage in aerobic exercise (like jogging, swimming, or brisk walking), your heart has to pump much faster to deliver oxygen to your working muscles. 
- Over time, this daily training makes the heart muscle thicker and stronger. 
- A strong heart can pump a larger volume of blood with every single beat (increased stroke volume), meaning it doesn't have to beat as fast when you are resting. 
- This significantly lowers your resting heart rate and reduces the daily wear and tear on the organ.

### Vascular Health
Exercise doesn't just help the heart muscle; it helps the plumbing. Physical activity stimulates the production of nitric oxide in your blood vessels, a molecule that helps them expand and stay flexible. This naturally lowers blood pressure. Exercise also actively raises your "good" HDL cholesterol while sweeping away triglycerides.

### The Prescription
The American Heart Association strongly recommends at least 150 minutes per week of moderate-intensity aerobic activity, or 75 minutes of vigorous activity. 
- You can break this down into just 30 minutes a day, five days a week. 
- Even simple changes—like taking the stairs instead of the elevator, parking further away from the store, or going for a 15-minute walk after dinner—can significantly benefit your heart over time.
        """.trimIndent(),
        color = HighOrange,
        icon = Icons.Default.Favorite,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Simple Daily Tips to Keep Your Heart Strong",
        content = """
## Small Changes, Big Results
Protecting your heart doesn't require a radical, stressful life overhaul. In fact, the most sustainable and effective changes are small, daily habits that compound over time. Here are practical tips you can easily start implementing today.

### Stand Up and Stretch
If you have an office desk job, you are likely sitting for 8 or more hours a day. Prolonged sitting severely increases the risk of cardiovascular events, regardless of whether you work out later in the evening. 
- Set a timer on your phone to stand up, stretch your legs, and walk around for two minutes every single hour.

### Swap Your Snacks
Instead of reaching for a bag of potato chips or a sugary candy bar in the afternoon slump, swap it for a handful of unsalted almonds or walnuts. 
- Nuts are packed with heart-healthy unsaturated fats and fiber that keep you full and lower cholesterol naturally.

### Practice Deep Breathing
Just five minutes of deep, diaphragmatic breathing a day can drastically lower your blood pressure and reduce stress hormones in your bloodstream. 
- Try the 4-7-8 method: inhale deeply for 4 seconds, hold your breath for 7 seconds, and exhale slowly for 8 seconds.

### Brush and Floss
Surprisingly, oral health is directly and intrinsically linked to heart health. 
- Bacteria from inflamed, bleeding gums can enter the bloodstream, elevating C-reactive protein (a marker for dangerous inflammation in the blood vessels) and increasing the risk of heart disease and stroke. 
- Floss daily and brush twice a day to protect both your smile and your heart.
        """.trimIndent(),
        color = ElevatedYellow,
        icon = Icons.Default.Info,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Warning Signs of a Heart Attack: What Everyone Should Know",
        content = """
## Every Minute Counts
A heart attack (myocardial infarction) occurs when the blood flow that brings oxygen to the heart muscle is severely reduced or cut off completely, usually by a blood clot. Knowing the warning signs and acting immediately can literally mean the difference between life and death. The faster you get to an emergency room, the less permanent damage your heart muscle will suffer.

### The Classic Symptoms
The most common symptoms are widely known, but often dismissed by patients who hope it’s just indigestion.
- **Chest Discomfort:** Most heart attacks involve discomfort in the center or left side of the chest that lasts for more than a few minutes or goes away and comes back. It can feel like uncomfortable pressure, squeezing, fullness, or pain.
- **Radiating Pain:** Pain or discomfort that spreads from the chest to the shoulders, arms (especially the left arm), back, neck, or jaw.
- **Shortness of Breath:** This often comes along with chest discomfort, but it can also occur before the chest pain begins.

### Atypical Symptoms (Especially in Women)
While chest pressure is the most common symptom for everyone, women are somewhat more likely than men to experience some of the other, more subtle symptoms. Because these aren't "classic" TV-movie heart attack signs, they are often fatally ignored.
- **Cold Sweat:** Breaking out in a sudden, unexplained cold sweat.
- **Nausea or Lightheadedness:** Feeling severely nauseous, vomiting, or suddenly feeling faint and dizzy for no apparent reason.
- **Extreme Fatigue:** A sudden, overwhelming exhaustion that makes it difficult to even walk across the room.

### What to Do
If you suspect you or someone else is having a heart attack, **do not drive yourself to the hospital.** Call 911 immediately. Emergency medical services (EMS) staff can begin treatment the moment they arrive—up to an hour sooner than if you drive yourself. While waiting for the ambulance, chew and swallow an aspirin (unless you are allergic), which helps keep your blood from clotting further.
        """.trimIndent(),
        color = HighOrange,
        icon = Icons.Default.Warning,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "How Smoking Affects Your Heart and Blood Vessels",
        content = """
## The Toxic Inhalation
Everyone knows that smoking destroys your lungs, but its effect on your cardiovascular system is just as devastating, if not more so. Smoking is a major cause of cardiovascular disease (CVD) and causes one of every three deaths from CVD.

### The Chemical Assault
Cigarette smoke contains thousands of toxic chemicals, including nicotine and carbon monoxide, which directly damage the delicate cells that line your blood vessels. 
- **Sticky Blood:** Smoking changes the chemistry of your blood, making it thicker, stickier, and far more prone to forming dangerous clots that can block blood flow to your heart or brain.
- **Plaque Formation:** The chemicals in smoke make the walls of your arteries rough and inflamed, creating the perfect environment for cholesterol and plaque to build up quickly (atherosclerosis).
- **Oxygen Deprivation:** Carbon monoxide from the smoke binds to your red blood cells, displacing the oxygen your organs desperately need. To compensate for the lack of oxygen, your heart is forced to beat much faster and pump harder.

### The Impact on Blood Pressure
Nicotine is a powerful stimulant. Every time you inhale a cigarette, your heart rate and blood pressure instantly spike. While this spike is temporary, smoking throughout the day keeps your blood pressure artificially elevated, putting constant, unnatural stress on your heart muscle.

### The Good News: Quitting
The human body has an incredible capacity to heal if you give it the chance. 
- Just 20 minutes after quitting, your heart rate and blood pressure drop.
- Within 12 hours, the carbon monoxide levels in your blood return to normal.
- Within 1 year of quitting, your added risk of coronary heart disease drops by 50%.
- Within 15 years, your risk of coronary heart disease is exactly the same as a non-smoker's. It is never too late to quit.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Info,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "The Role of Hydration in Heart Health",
        content = """
## The Fluid Mechanics of the Heart
We often associate hydration with athletic performance or skin health, but drinking enough water is actually one of the easiest and most critical things you can do to support your cardiovascular system.

### How Water Helps Your Heart
Your heart is a pump, and your blood is the fluid it is pumping. 
- Blood is made up of about 50% water. 
- When you are well-hydrated, your blood volume is optimal, meaning the blood is thin and flows easily through your vast network of veins and arteries. 
- Because the blood flows smoothly, your heart doesn't have to work very hard to push it along.

### The Danger of Dehydration
When you become dehydrated, the amount of blood circulating through your body (blood volume) actually decreases. 
- To compensate for the low volume and maintain blood pressure, your body constricts (narrows) your blood vessels and your heart beats faster. 
- Furthermore, the remaining blood becomes thicker and more concentrated with sodium, making it sludgy and much harder to pump. This puts an immense, unnecessary strain on your heart muscle and increases the risk of blood clots.

### How Much Should You Drink?
The old rule of "eight glasses a day" is a good starting point, but your true hydration needs depend on your weight, the climate you live in, and how much you exercise. 
- A better rule of thumb is to drink enough water so that your urine is clear or very pale yellow. 
- Pay special attention during hot weather or when exercising; if you feel thirsty, you are already mildly dehydrated. 
- **Important Note:** If you have been diagnosed with severe heart failure, your doctor may actually put you on a fluid restriction to prevent fluid from backing up into your lungs. Always follow your cardiologist's specific advice regarding fluid intake.
        """.trimIndent(),
        color = ElevatedYellow,
        icon = Icons.Default.Lightbulb,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "Understanding Arrhythmia: Causes, Symptoms, and Treatment",
        content = """
## When the Rhythm is Broken
An arrhythmia is an abnormality in the timing or pattern of the heartbeat. Your heart is controlled by a highly complex electrical system. When that system malfunctions, the heart can beat too fast (tachycardia), too slow (bradycardia), or with an irregular, chaotic rhythm.

### Common Types of Arrhythmia
There are many types of arrhythmias, ranging from completely harmless to life-threatening.
- **Atrial Fibrillation (AFib):** This is the most common type of serious arrhythmia. The upper chambers of the heart (atria) beat erratically and out of coordination with the lower chambers. This causes blood to pool in the heart, drastically increasing the risk of a blood clot forming and causing a stroke.
- **Premature Ventricular Contractions (PVCs):** These are the "skipped beats" or "flutters" you sometimes feel in your chest. They are extremely common and usually harmless, often triggered by stress, caffeine, or lack of sleep.

### Symptoms to Watch For
Some arrhythmias cause no symptoms and are only discovered during a routine EKG. When symptoms do occur, they may include:
- A fluttering sensation or a "thumping" in your chest.
- A racing heartbeat or a heartbeat that feels unusually slow.
- Dizziness, lightheadedness, or feeling like you might pass out.
- Shortness of breath or chest pain.

### Treatment Options
Treatment depends entirely on the type and severity of the arrhythmia. 
- Harmless arrhythmias (like occasional PVCs) may require no treatment at all other than cutting back on caffeine. 
- For dangerous arrhythmias like AFib, doctors may prescribe blood thinners to prevent strokes, or medications to control the heart rate. 
- In some cases, a procedure called an ablation (which destroys the tiny patch of heart tissue causing the electrical short circuit) or the surgical implantation of a pacemaker or defibrillator is necessary to keep the heart beating perfectly on time.
        """.trimIndent(),
        color = NormalGreen,
        icon = Icons.Default.Warning,
        category = ArticleCategory.HEART_HEALTH
    ),
    Article(
        title = "How to Improve Heart Health After Age 40",
        content = """
## The Critical Decade
Turning 40 is a major milestone, but it is also the decade where your cardiovascular risk begins to rise significantly. The metabolism slows down, blood vessels begin to naturally stiffen, and bad habits from your 20s and 30s start to catch up with you. However, the 40s are also the perfect time to make proactive changes that will protect your heart for the rest of your life.

### Schedule a Comprehensive Baseline
If you haven't been going to the doctor regularly, age 40 is the time to start. You need a complete cardiovascular baseline. 
- Have your doctor check your fasting blood glucose, a full lipid panel (cholesterol and triglycerides), and your resting blood pressure. 
- Discuss your family history of heart disease in detail. If your father had a heart attack at 45, you need to be exceptionally aggressive with your preventative care right now.

### Re-evaluate Your Diet and Weight
You can no longer eat like you did in your 20s. As your metabolism slows, those extra calories turn into visceral fat (belly fat), which surrounds your organs and actively pumps inflammatory chemicals into your bloodstream, damaging your heart. 
- Cut back on refined carbohydrates and added sugars.
- Focus heavily on lean proteins, massive amounts of vegetables, and heart-healthy fats like olive oil and avocados.

### Prioritize Strength Training
While cardiovascular exercise (running, biking) is crucial for heart health, strength training becomes incredibly important after 40. 
- You naturally lose muscle mass as you age. Lifting weights or doing bodyweight exercises twice a week builds muscle, which acts as a metabolic engine to burn glucose and improve insulin sensitivity.

### Listen to Your Body
In your 20s, you could ignore a random ache or pain. In your 40s, you must listen to the subtle signals your body sends. If you feel unusually fatigued, if you get out of breath faster than you used to, or if you feel strange flutters in your chest, do not ignore them. Early detection is the key to surviving and thriving with heart disease.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Info,
        category = ArticleCategory.HEART_HEALTH
    )
)
