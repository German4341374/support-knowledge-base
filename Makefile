MVNW := ./mvnw

.PHONY: setup format lint test build run up down logs clean

setup:
	chmod +x mvnw
	$(MVNW) --batch-mode --no-transfer-progress dependency:go-offline

format:
	$(MVNW) --batch-mode --no-transfer-progress spotless:apply

lint:
	$(MVNW) --batch-mode --no-transfer-progress spotless:check
	$(MVNW) --batch-mode --no-transfer-progress compile

test:
	$(MVNW) --batch-mode --no-transfer-progress verify

build:
	$(MVNW) --batch-mode --no-transfer-progress package -DskipTests

run:
	$(MVNW) spring-boot:run -Dspring-boot.run.profiles=dev

up:
	docker compose up --build --detach
	docker compose ps

down:
	docker compose down

logs:
	docker compose logs --follow app

clean:
	docker compose down --volumes --remove-orphans
	$(MVNW) clean

