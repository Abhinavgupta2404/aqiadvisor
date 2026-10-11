# Delhi Air Advisor — "Should I Step Out?"

A web app that turns live Delhi air quality data into plain-language advice on whether it's safe to go outside.

Built solo for **Environmental Hacks** (WeMakeDevs × AWS, Bharat Builds Tour) — **Air track**.

**Live app:** http://aqi-advisor-env.eba-9vmpx35m.ap-south-1.elasticbeanstalk.com
**Demo video:** [add YouTube link]
**Blog post:** [add AWS Builder Center link]

## The problem

An AQI number like "312" doesn't tell most people what to do. Should the kids play outside? Is it okay to go for a run? This app answers that directly.

## What it does

1. Pick an area in Delhi from the list.
2. The app fetches the latest reading from the nearest monitoring station.
3. It shows the AQI, the official category, and clear advice, in a colour-coded result.

Categories follow the Indian CPCB AQI scale:

| AQI | Category |
|---|---|
| 0–50 | Good |
| 51–100 | Satisfactory |
| 101–200 | Moderate |
| 201–300 | Poor |
| 301–400 | Very Poor |
| 401–500 | Severe |

## Tech stack

- **Backend:** Java, Spring Boot (REST endpoint `/api/aqi`)
- **Frontend:** plain HTML, CSS and JavaScript (served by Spring Boot)
- **Data:** [WAQI API](https://aqicn.org/api/), which aggregates CPCB and Delhi Pollution Control Committee station data
- **Hosting:** AWS Elastic Beanstalk (Corretto, Amazon Linux 2023)

## API

```
GET /api/aqi?lat=28.7159&lon=77.1157
```

Example response:

```json
{
  "aqi": 114,
  "category": "Moderate",
  "advice": "Generally okay, but people with existing breathing or heart conditions should reduce prolonged outdoor activity."
}
```

## Run locally

Requirements: JDK 21 or newer, and a free WAQI token from https://aqicn.org/data-platform/token/

```bash
cd aqiadvisor        # the folder that contains pom.xml
```

Windows (PowerShell):
```powershell
$env:WAQI_TOKEN="your_token_here"
.\mvnw.cmd spring-boot:run
```

macOS / Linux:
```bash
export WAQI_TOKEN=your_token_here
./mvnw spring-boot:run
```

Then open http://localhost:8080

## Deploy on AWS Elastic Beanstalk

1. Build the jar: `.\mvnw.cmd clean package -DskipTests`
2. Create an Elastic Beanstalk application with a Java (Corretto) platform and upload `target/aqiadvisor-0.0.1-SNAPSHOT.jar`.
3. Add environment properties:
   - `WAQI_TOKEN` = your token (the key is never stored in the code)
   - `SERVER_PORT` = `5000` (Elastic Beanstalk forwards traffic to port 5000)

## Known limitations

- The live link is HTTP only, so some phone browsers that try HTTPS first may fail to open it. A CloudFront or API Gateway HTTPS front is the planned fix.
- Readings come from the nearest station, which may be some distance from the selected area.

## Future work

- Advice tailored to kids, runners and elderly people
- "Use my location" button
- Daily alerts using EventBridge and SNS

## Credits

Air quality data: World Air Quality Index Project (WAQI), CPCB and DPCC.
Built by Abhinav Gupta.
