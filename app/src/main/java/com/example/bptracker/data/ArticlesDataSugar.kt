package com.example.bptracker.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.bptracker.theme.*

val sugarArticles = listOf(
    Article(
        title = "Understanding Blood Sugar Levels: A Complete Guide",
        content = """
## The Foundation of Energy
Blood sugar, or glucose, is the main sugar found in your blood. It comes from the food you eat and is your body's primary source of energy. Your blood carries glucose to all of your body's cells to use for energy, allowing you to move, think, and function.

### The Role of Insulin
Insulin is a hormone produced by the pancreas. It acts exactly like a key that unlocks the door to your cells, allowing glucose to enter. 
- When you have diabetes or prediabetes, your body either doesn't produce enough insulin (Type 1) or can't use it effectively (Type 2). 
- As a result, glucose builds up in your blood instead of entering your cells. This starves your cells of energy while simultaneously creating a toxic environment in your bloodstream.

### Why Tracking Matters
Maintaining healthy blood sugar levels is crucial for overall health and longevity. Consistently high levels can lead to serious long-term complications over time. 
- High blood sugar can cause nerve damage (neuropathy), which often leads to tingling or numbness in the hands and feet.
- It can cause severe kidney disease (nephropathy) and vision problems (retinopathy), potentially leading to blindness.
- It exponentially increases the risk of cardiovascular issues, including heart attacks and strokes.

Understanding the difference between fasting blood sugar and post-meal spikes is the first step in managing your metabolic health. Regular monitoring helps you and your doctor understand how different foods, exercises, and medications affect your unique physiology, empowering you to make targeted lifestyle changes.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Info,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Normal Blood Sugar Ranges for Adults",
        content = """
## Knowing Your Numbers
Understanding normal blood sugar ranges is absolutely vital for managing your health, whether you have been diagnosed with diabetes or are just monitoring your metabolic wellness proactively. These numbers provide a baseline for your daily tracking.

### Fasting Blood Sugar
A fasting blood sugar test is typically taken in the morning before you have eaten or had anything to drink (other than water) for at least 8 to 10 hours. This gives a pure look at your body's baseline state.
- **Normal:** Between 70 and 99 mg/dL (3.9 and 5.5 mmol/L).
- **Prediabetes:** Between 100 and 125 mg/dL (5.6 and 6.9 mmol/L). This is a warning sign that action needs to be taken.
- **Diabetes:** 126 mg/dL (7.0 mmol/L) or higher, usually confirmed on two separate tests.

### Postprandial (After-Meal) Blood Sugar
This measurement is typically taken exactly two hours after the start of a meal. It is used to see how your body processes carbohydrates and how efficiently your insulin clears glucose from your blood.
- **Normal:** Less than 140 mg/dL (7.8 mmol/L).
- **Prediabetes:** Between 140 and 199 mg/dL (7.8 and 11.0 mmol/L).
- **Diabetes:** 200 mg/dL (11.1 mmol/L) or higher.

### A1C Levels
Your doctor may also measure your A1C, which is a blood test that provides your average blood sugar levels over the past 3 months.
- **Normal:** Below 5.7%
- **Prediabetes:** 5.7% to 6.4%
- **Diabetes:** 6.5% or higher

*Note: Target ranges can vary based on individual health conditions, age, and pregnancy status. Always consult with your healthcare provider to determine your personal target range.*
        """.trimIndent(),
        color = NormalGreen,
        icon = Icons.Default.Favorite,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Signs and Symptoms of High Blood Sugar",
        content = """
## Recognizing Hyperglycemia
Hyperglycemia occurs when there is too much sugar in your blood. This usually happens when your body has too little insulin or isn't using insulin properly. Recognizing the early warning signs can prevent severe medical complications and allow you to take action before organ damage occurs.

### Early Warning Signs (The Three Ps)
The classic symptoms of high blood sugar are often remembered by medical professionals as the "Three Ps":
- **Polydipsia:** Excessive and unquenchable thirst. You may feel like you can't drink enough water to satisfy your throat.
- **Polyuria:** Frequent urination, especially waking up multiple times at night. This happens because your kidneys are working overtime to flush out the excess sugar through your urine.
- **Polyphagia:** Extreme hunger, even shortly after eating. Because your cells are resistant to insulin, they are starved of glucose, tricking your brain into thinking you are starving.

### Other Common Symptoms
Beyond the classic three, you might experience several other disruptive symptoms:
- **Blurry vision:** High sugar levels pull fluid from your tissues, including the lenses of your eyes, making it hard to focus.
- **Unexplained fatigue:** Without glucose entering your cells, you have no energy.
- **Unintentional weight loss:** Your body starts burning fat and muscle for energy because it can't use glucose.
- **Frequent infections:** Sugar feeds bacteria and yeast, leading to frequent thrush or skin infections.
- **Slow-healing cuts:** High blood sugar impairs blood circulation and immune function.

If left untreated over a long period, chronic hyperglycemia can cause toxic acids (ketones) to build up in your blood and urine (ketoacidosis), which is a medical emergency. If you consistently experience these symptoms, consult a doctor immediately.
        """.trimIndent(),
        color = HighOrange,
        icon = Icons.Default.Warning,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Signs and Symptoms of Low Blood Sugar",
        content = """
## The Dangers of Hypoglycemia
Hypoglycemia occurs when your blood sugar drops below normal levels, typically defined as less than 70 mg/dL (3.9 mmol/L). Unlike high blood sugar, which damages the body slowly over time, low blood sugar is an immediate, acute crisis that requires fast action to prevent neurological damage.

### Immediate Symptoms (The Adrenaline Rush)
When your brain starts to run out of glucose, the body panics and releases adrenaline as a defense mechanism. This causes rapid, noticeable symptoms that can feel like a panic attack:
- Shaking, trembling, or feeling extremely jittery.
- Profuse sweating or sudden chills.
- Rapid heartbeat (palpitations) pounding in your chest.
- Intense hunger or sudden nausea.
- Feeling dizzy, lightheaded, or faint when standing.
- Anxiety, irritability, or sudden mood changes (often referred to as being "hangry").

### Severe Symptoms (Medical Emergency)
If the blood sugar level continues to drop and the brain is severely deprived of glucose, the symptoms become neurologically severe. At this stage, the person may not be able to help themselves:
- Confusion or inability to concentrate on simple tasks.
- Slurred speech or stumbling.
- Clumsiness or jerky, uncoordinated movements.
- Seizures, convulsions, or total loss of consciousness.

### The Rule of 15 for Treatment
If you experience hypoglycemia, treat it immediately using the medical "Rule of 15":
- Consume exactly **15 grams** of fast-acting carbohydrates (like a small box of apple juice, a tablespoon of honey, or 4 glucose tablets). Avoid chocolate or fat-heavy snacks, as fat slows down sugar absorption.
- Wait exactly **15 minutes**, then check your blood sugar again. 
- If it is still below 70 mg/dL, repeat the process. Once your blood sugar is stable, eat a small meal with protein to prevent another crash.
        """.trimIndent(),
        color = ElevatedYellow,
        icon = Icons.Default.Warning,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "How Often Should You Check Your Blood Sugar?",
        content = """
## Tailoring Your Testing Schedule
The frequency of blood sugar testing depends entirely on the type of diabetes you have, your treatment plan, and your personal health goals. Testing provides critical data to help you and your doctor make decisions about food, medication, and exercise. It is not a one-size-fits-all approach.

### Type 1 Diabetes
People with Type 1 diabetes rely on injected insulin and typically need to check their blood sugar multiple times a day to avoid extreme highs and lows. Your doctor may recommend testing:
- Immediately before meals and snacks.
- Before and after engaging in physical exercise.
- Right before going to bed.
- Sometimes during the night to catch nocturnal hypoglycemia.
- More frequently if you are ill, changing your routine, or beginning a new medication.
*Note: Many people with Type 1 now use Continuous Glucose Monitors (CGMs) to receive minute-by-minute data and reduce the need for frequent finger sticks.*

### Type 2 Diabetes
Testing frequency for Type 2 diabetes varies widely:
- **Insulin Users:** If you take insulin, your doctor may recommend testing multiple times a day, similar to Type 1.
- **Oral Medications:** If you manage Type 2 diabetes with non-insulin medications, you might only need to test once or twice a day.
- **Diet and Exercise:** If you manage it with diet and exercise alone, you may not need to test daily. However, checking during periods of illness, high stress, or when experimenting with a new diet is highly beneficial.

### Prediabetes
For prediabetes, daily tracking isn't usually required. However, occasional fasting checks in the morning, and post-meal checks two hours after eating, can help you understand how specific dietary changes (like cutting out soda or eating more fiber) are impacting your metabolic health and reversing the condition.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Lightbulb,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Best Foods for Maintaining Healthy Blood Sugar Levels",
        content = """
## Eating for Metabolic Stability
The food you eat has a direct and immediate impact on your blood sugar levels. Focusing on a diet rich in whole, unprocessed foods can help stabilize your glucose and prevent dangerous spikes and crashes throughout the day.

### The Power of Dietary Fiber
Dietary fiber is your best friend when managing blood sugar. Because the body cannot digest fiber, it slows down the digestion of carbohydrates and the absorption of sugar, leading to a much more gradual and safe rise in blood sugar levels. Excellent sources include:
- **Whole grains:** Oats, quinoa, barley, and brown rice.
- **Legumes:** Beans, lentils, and chickpeas (which also provide excellent protein).
- **Non-starchy vegetables:** Broccoli, spinach, cauliflower, and Brussels sprouts.
- **Whole fruits:** Especially berries and apples (make sure to eat the skin, where the fiber is!).

### Healthy Fats and Lean Proteins
Pairing carbohydrates with healthy fats or lean proteins is a fantastic strategy to blunt blood sugar spikes. For example, eating an apple with a handful of almonds is much better for your blood sugar than eating an apple completely alone. 
- **Fats:** Avocados, olive oil, and nuts/seeds (chia, flax, walnuts).
- **Proteins:** Lean meats (chicken, turkey), fish rich in omega-3s (salmon), eggs, and unsweetened Greek yogurt.

### Foods to Strictly Avoid
Minimize your intake of refined carbohydrates and added sugars. These are digested rapidly and cause massive, immediate blood sugar spikes, followed by exhausting crashes. 
- **Sugary beverages:** Soda, sweetened teas, and excessive fruit juices.
- **Refined carbs:** White bread, white pasta, and pastries.
- **Heavily processed snacks:** Chips, crackers, and candy. 
Always read nutrition labels to check for hidden "added sugars" in seemingly healthy foods like granola bars and yogurt.
        """.trimIndent(),
        color = NormalGreen,
        icon = Icons.Default.Favorite,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "How Exercise Affects Blood Sugar Levels",
        content = """
## Movement as Medicine
Physical activity is one of the most effective ways to manage blood sugar naturally. Exercise acts almost like invisible medication; it forces your muscles to consume glucose for energy and makes your cells significantly more sensitive to insulin.

### Immediate Effects of Exercise
When you exercise, your muscles need immediate energy to contract. To meet this demand, your body burns the glucose stored in your muscles and liver. Once that is depleted, it pulls glucose directly out of your bloodstream. 
- This lowers your blood sugar almost immediately during a workout. 
- The blood-sugar-lowering effect can actually last for 24 hours or more after a strenuous workout as your body works to rebuild its glycogen stores.

### Long-Term Metabolic Benefits
Consistent exercise significantly improves your body's overall insulin sensitivity. This means your cells are better able to use available insulin to take up glucose during and after activity. This is incredibly beneficial for reversing prediabetes or managing Type 2 diabetes over the long haul. Building muscle mass also gives your body a larger "storage tank" for glucose.

### A Word of Caution for Insulin Users
If you take insulin or insulin-stimulating oral medications, exercise can sometimes cause your blood sugar to drop dangerously low (hypoglycemia). 
- Always check your blood sugar before a workout. 
- If it's below 100 mg/dL, eat a small carbohydrate snack (like half a banana) before beginning. 
- Conversely, extremely intense, maximum-effort exercise (like heavy weightlifting or sprinting) can actually cause a temporary spike in blood sugar due to the massive release of stress hormones like adrenaline, which tell your liver to dump glucose into the blood. Monitor your levels to see how your body reacts to different exercises.
        """.trimIndent(),
        color = HighOrange,
        icon = Icons.Default.Info,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Prediabetes: Early Warning Signs and Prevention Tips",
        content = """
## The Critical Crossroads
Prediabetes means your blood sugar level is higher than normal, but not yet high enough to be classified as Type 2 diabetes. It is a critical crossroads; without lifestyle changes, adults and children with prediabetes are at exceptionally high risk of developing full-blown Type 2 diabetes within five years.

### The Silent Condition
Prediabetes often has absolutely no clear symptoms. You can have it for years without knowing it, which is why regular medical checkups are so vital. 
- **Acanthosis Nigricans:** Some people may experience darkened, velvety patches of skin on certain parts of the body, such as the neck, armpits, and elbows. This is a physical sign of insulin resistance.
- The only definitive way to know for sure is to get a blood test (like an A1C test or fasting blood glucose test) at your doctor's office.

### Reversing the Trend
The incredibly good news is that prediabetes is often 100% reversible. It is not a life sentence. You can halt the progression to Type 2 diabetes through dedicated lifestyle changes:
- **Lose Excess Weight:** You don't have to become a bodybuilder. Losing just 5% to 7% of your total body weight (about 10 to 14 pounds for a 200-pound person) can dramatically reduce your risk.
- **Move More:** Aim for 30 minutes of moderate physical activity (like brisk walking, cycling, or swimming) at least 5 days a week.
- **Eat Healthier:** Focus on high-fiber foods, lean proteins, and complex carbohydrates while cutting out sugary drinks, alcohol, and refined snacks.

Think of prediabetes as a warning bell—it’s your body telling you it’s time to make a change before permanent damage occurs. Take action today.
        """.trimIndent(),
        color = ElevatedYellow,
        icon = Icons.Default.Warning,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Tips for Managing Diabetes Through Lifestyle Changes",
        content = """
## A Holistic Approach to Health
Living with diabetes requires daily management, but it doesn't mean you can't live a full, active, and long life. A holistic approach that combines your doctor's medical treatment with smart lifestyle choices is the absolute key to success and avoiding complications.

### Stress Management
Stress isn't just in your head; it has a profound, measurable physiological effect. 
- When you are stressed, your body releases hormones like cortisol and adrenaline. 
- These hormones trigger your liver to release stored glucose into your bloodstream to give you energy to "fight or flee," which can cause severe blood sugar spikes even if you haven't eaten anything. 
- Incorporate relaxation techniques like deep breathing, yoga, or daily nature walks into your routine to keep stress at bay.

### Sleep Hygiene
Poor sleep impairs your body's ability to use insulin effectively and increases stress hormones the next day. 
- Aim for 7 to 9 hours of quality, uninterrupted sleep per night. 
- Establish a consistent sleep schedule (going to bed at the exact same time every night) and limit screen time (phones, TVs) an hour before bed to improve your rest.

### Hydration
Drinking enough water is a highly underrated management tool. 
- When your blood sugar is high, your body tries to flush the excess glucose out through urine. 
- If you aren't drinking enough water, you become dehydrated. Dehydration actually concentrates the sugar in your blood, making the reading even higher and thicker. 
- Drink plenty of pure water throughout the day, strictly avoiding sugary sodas or excessive caffeine, which can further dehydrate you.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Lightbulb,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Why Keeping a Blood Sugar Log Is Important",
        content = """
## The Power of Data
A blood sugar log is much more than just a tedious list of numbers; it is a critical diagnostic tool that reveals the unique, hidden patterns of your personal metabolism. Using a tracker app to record this data is the smartest way to take control of your health.

### Discovering Your Triggers
Everyone’s body reacts differently to various foods. A bowl of oatmeal might be perfectly fine for one person but cause a massive, dangerous spike for another. 
- By logging your blood sugar alongside notes about what you ate or how you exercised, you begin to see clear patterns. 
- You can identify exactly which foods cause trouble and which meals keep your energy stable throughout the afternoon.

### Empowering Your Doctor
When you visit your healthcare provider, simply telling them "my blood sugar was high a few times" isn't very helpful for prescribing medication. 
- Handing them a detailed log showing your fasting numbers, post-meal spikes, and times of day allows them to make incredibly precise adjustments to your insulin or oral medication. 
- It takes the guesswork out of your treatment plan and prevents dangerous over-medication.

### Motivation and Accountability
Managing a chronic condition can cause burnout, but seeing your data visually tracked in charts and graphs is highly motivating. 
- When you make a positive lifestyle change—like committing to a 15-minute walk after dinner every night—you can physically see the lines on your chart go down. 
- Tracking keeps you accountable to your goals and provides a profound sense of achievement when your numbers finally stabilize. Keep tracking, and stay healthy!
        """.trimIndent(),
        color = NormalGreen,
        icon = Icons.Default.Info,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "How Sleep Affects Blood Sugar Levels",
        content = """
## The Nighttime Metabolic Shift
Most people think of diet and exercise as the primary controllers of blood sugar, but sleep plays an equally vital, often overlooked role in metabolic health. How well you sleep directly impacts how efficiently your body uses insulin the very next day.

### The Cortisol Connection
When you don't get enough high-quality sleep (typically defined as less than 7 hours per night for an adult), your body experiences it as physical stress. 
- In response to this stress, your body pumps out extra cortisol, the primary stress hormone. 
- Cortisol's main job is to ensure you have enough energy to handle the "stress" by telling your liver to dump stored glucose directly into your bloodstream.
- Even if you haven't eaten anything, a bad night of sleep can cause you to wake up with inexplicably high fasting blood sugar.

### Insulin Resistance
Lack of sleep doesn't just increase sugar in the blood; it actively prevents your body from clearing it out. 
- Studies have shown that just a few nights of severe sleep deprivation can cause your cells to become significantly less sensitive to insulin (temporary insulin resistance). 
- This means your pancreas has to work much harder, pumping out more and more insulin just to push the same amount of glucose into your cells. Over time, this exhausts the pancreas and directly contributes to Type 2 diabetes.

### Sleep Apnea
Sleep apnea is a condition where you repeatedly stop breathing for short periods during the night. 
- It is highly correlated with Type 2 diabetes and obesity. Every time you stop breathing, your oxygen levels drop, causing massive spikes in stress hormones and blood sugar. 
- If you snore heavily or constantly feel exhausted despite sleeping a full 8 hours, ask your doctor for a sleep study. Treating sleep apnea with a CPAP machine often dramatically improves blood sugar control.
        """.trimIndent(),
        color = HighOrange,
        icon = Icons.Default.Warning,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Blood Sugar Spikes: Causes and Prevention Tips",
        content = """
## Riding the Rollercoaster
A blood sugar spike occurs when glucose builds up rapidly in your bloodstream, typically after eating. While a small rise in blood sugar is completely normal after a meal, extreme spikes (and the inevitable crashes that follow) are dangerous. They damage blood vessels, exhaust your pancreas, and leave you feeling incredibly fatigued and hungry.

### The Primary Causes
The most obvious cause of a spike is consuming a large amount of carbohydrates, especially refined or simple carbs. 
- **Refined Carbs:** White bread, white rice, pasta, and pastries are digested almost instantly, dumping glucose into your blood all at once.
- **Liquid Sugar:** Sugary sodas, sweet teas, and even 100% fruit juices are the worst offenders because they don't even need to be digested; the sugar is absorbed straight through the stomach lining into the blood.
- **Other Causes:** As mentioned in previous articles, intense stress, illness, lack of sleep, or skipping a dose of medication can also cause sudden spikes.

### The Power of Food Pairing
You don't necessarily have to eliminate all carbohydrates to prevent spikes; you just need to change how you eat them. 
- **The Golden Rule:** Never eat a naked carbohydrate. Always pair your carbs with a source of dietary fiber, healthy fat, or lean protein. 
- Fat and protein take a very long time to digest. When they are mixed with carbohydrates in your stomach, they act like a roadblock, slowing down the digestion of the carbs and tricking your body into absorbing the sugar slowly and steadily. 

### Timing Your Meals
Eating large, heavy meals puts a massive burden on your metabolic system all at once. 
- Instead of three massive meals, try eating smaller, well-balanced meals spaced evenly throughout the day. 
- Additionally, taking a 10 to 15-minute brisk walk immediately after eating a meal is clinically proven to blunt post-meal blood sugar spikes because your leg muscles immediately burn off the incoming glucose before it can build up in the blood.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Info,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Understanding HbA1c and Why It Matters",
        content = """
## The Big Picture of Blood Sugar
Checking your blood sugar with a daily finger stick is like taking a single snapshot of your metabolic health at one precise moment in time. While those snapshots are crucial, they don't tell the whole story. The HbA1c (or simply A1C) test provides the wide-angle, panoramic view.

### What is A1C?
Inside your red blood cells is a protein called hemoglobin, which carries oxygen. 
- When sugar enters your bloodstream, it naturally attaches itself to this hemoglobin. 
- Once the sugar is attached, it stays there for the entire lifespan of the red blood cell, which is about 3 months. 
- The A1C test measures the percentage of your hemoglobin proteins that are coated with sugar. Because the cells live for 3 months, the test gives your doctor a highly accurate average of what your blood sugar has been over the past 90 days.

### What Do the Numbers Mean?
Unlike daily readings measured in mg/dL, the A1C is reported as a percentage.
- **Normal:** Below 5.7%.
- **Prediabetes:** 5.7% to 6.4%. This is the critical window where lifestyle changes can completely reverse the trajectory.
- **Diabetes:** 6.5% or higher on two separate tests indicates diabetes. 
- If you have already been diagnosed with diabetes, your doctor will likely set a personalized target for you, often around 7.0%, to minimize the risk of long-term complications.

### Why It Is Crucial
The A1C test is the gold standard for predicting your risk of long-term diabetic complications. 
- Daily finger sticks can be easily manipulated (for example, if you eat perfectly for just three days before your doctor's appointment, your daily numbers will look great). 
- The A1C test cannot be tricked. It reveals exactly how well your treatment plan (medication, diet, and exercise) is working over the long term. If your A1C is creeping up, it is a clear signal that your current management strategy needs an immediate adjustment.
        """.trimIndent(),
        color = ElevatedYellow,
        icon = Icons.Default.Lightbulb,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "How Illness and Infection Can Affect Blood Sugar",
        content = """
## The Body Under Attack
Managing blood sugar is a daily routine that usually follows predictable patterns. However, when you catch a cold, the flu, or develop an infection, those predictable patterns get thrown completely out the window. Illness creates metabolic chaos, and you must be prepared to adjust your management strategy accordingly.

### The Stress Response to Sickness
When a virus or bacteria invades your body, your immune system launches a massive counterattack to fight it off. 
- This immune response is incredibly stressful on the body. 
- To fuel the battle, your body releases stress hormones like adrenaline and cortisol. 
- As we know, these hormones instruct the liver to dump stored glucose into the bloodstream to provide extra energy. 
- At the exact same time, these stress hormones make your cells highly resistant to insulin. The result? Your blood sugar skyrockets, even if you are too sick to eat a single bite of food.

### The Danger of Ketones
For people with Type 1 diabetes (and occasionally Type 2), the combination of severe insulin resistance and high blood sugar during an illness can lead to a dangerous condition called Diabetic Ketoacidosis (DKA). 
- Because the cells can't get glucose, the body starts burning fat at an alarming rate, releasing toxic acids called ketones into the blood. 
- If you are sick and your blood sugar remains above 240 mg/dL, you must test your urine for ketones using over-the-counter test strips.

### Sick Day Rules
You should establish "Sick Day Rules" with your endocrinologist before you ever get sick. General rules include:
- **Test more frequently:** Check your blood sugar every 2 to 4 hours instead of your usual schedule.
- **Keep taking medication:** Never stop taking your insulin or diabetes pills just because you aren't eating. In fact, because of the stress hormones, you may actually need *more* insulin than usual.
- **Stay hydrated:** Drink at least 8 ounces of sugar-free fluids (like water or broth) every hour to flush excess sugar out through your urine.
        """.trimIndent(),
        color = NormalGreen,
        icon = Icons.Default.Warning,
        category = ArticleCategory.BLOOD_SUGAR
    ),
    Article(
        title = "Common Myths and Facts About Diabetes",
        content = """
## Separating Fact from Fiction
Diabetes is one of the most common chronic diseases in the world, yet it is completely surrounded by persistent myths, misunderstandings, and outdated information. Believing these myths can lead to dangerous health decisions and unnecessary anxiety. Let's clear up the most common misconceptions.

### Myth 1: Eating Too Much Sugar Causes Diabetes
**Fact:** This is the most common myth of all. Eating a lot of sugar does not directly cause Type 1 diabetes, which is an autoimmune disease where the body attacks its own pancreas. For Type 2 diabetes, the answer is more complex. While consuming massive amounts of sugary drinks is linked to Type 2 diabetes, the sugar itself isn't the direct trigger. The trigger is the obesity and weight gain that results from consuming excess calories. Being overweight causes insulin resistance, which leads to Type 2 diabetes.

### Myth 2: People With Diabetes Can Never Eat Sweets
**Fact:** People with diabetes absolutely can eat sweets and desserts! They just have to be smart about it. Sweets must be eaten in moderation and carefully factored into their overall daily carbohydrate count. Pairing a small dessert with a protein-heavy meal helps slow the absorption of the sugar, preventing massive spikes. Total deprivation often leads to binge eating later.

### Myth 3: You Have to Lose 50 Pounds to See Results
**Fact:** You do not need to reach your "ideal" high school weight to drastically improve your diabetes. Studies conclusively prove that losing just 5% to 7% of your total body weight (which is only 10 to 14 pounds for a 200-pound person) significantly improves insulin sensitivity and lowers A1C. Small, sustainable weight loss is far more effective than crash dieting.

### Myth 4: Needing Insulin Means You Failed
**Fact:** For people with Type 2 diabetes, the pancreas naturally produces less and less insulin over time as the disease progresses, regardless of how perfectly you eat or exercise. Needing to start insulin therapy is not a personal failure; it is simply the natural progression of the disease and a necessary tool to keep your organs safe from toxic glucose levels.
        """.trimIndent(),
        color = PrimaryBlue,
        icon = Icons.Default.Info,
        category = ArticleCategory.BLOOD_SUGAR
    )
)
