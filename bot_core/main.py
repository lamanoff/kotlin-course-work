from fastapi import FastAPI
import uvicorn
from core import make_tags_embeddings
from api.asker_route import ask_route

app = FastAPI()

app.include_router(ask_route)


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", reload=True, port=8888)
